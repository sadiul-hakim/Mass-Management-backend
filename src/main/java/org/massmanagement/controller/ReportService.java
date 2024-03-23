package org.massmanagement.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.massmanagement.dto.CostDTO;
import org.massmanagement.dto.MealDTO;
import org.massmanagement.dto.UserDTO;
import org.massmanagement.model.TransactionType;
import org.massmanagement.service.*;
import org.massmanagement.util.DateFormatter;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportService {
    private final CostService costService;
    private final TransactionTypeService transactionTypeService;
    private final PeriodService periodService;
    private final MealService mealService;
    private final UserService userService;
    private final UserStatusService userStatusService;
    private final IncomeService incomeService;

    public Map<String, Object> generateReport() {
        log.info("Generating report.");

        Map<String, Object> report = new HashMap<>();

        var mealRate = calculateMealRate(report);
        report.put("mealRate", mealRate);

        long income = incomeService.getTotalAmount();
        long cost = costService.getTotalAmount();
        report.put("total_income",income);
        report.put("total_cost",cost);

        var users = userService.getAll();
        if(users.isEmpty()) return Collections.emptyMap();

        loadUserInfo(mealRate, report,users);

        return report;
    }

    private void loadUserInfo(double mealRate, Map<String, Object> report,List<UserDTO> users) {

        var marketType = transactionTypeService.getByTitle("Market");
        var electricityBillType = transactionTypeService.getByTitle("Electricity Bill");
        var depositType = transactionTypeService.getByTitle("Border Deposits");

        report.put("borders", new ArrayList<>());

        var otherCostsObj = costService.findByTypeNotIn(List.of(marketType.getId(), electricityBillType.getId()));

        double otherCosts = 0.0;
        for (CostDTO cost : otherCostsObj) {
            otherCosts += cost.getAmount();
        }
        report.put("other_cost", format(otherCosts, "0.00"));

        var date = LocalDate.now();
        report.put("date", DateFormatter.formatDate(date));

        for (UserDTO user : users) {

            Map<String, Object> userInfo = new HashMap<>();

            Map<String, Object> userData = new HashMap<>();
            userData.put("name", user.getName());
            userData.put("id", user.getId());

            userInfo.put("border", userData);

            long meals;
            if (user.getStatus().getStatus().equalsIgnoreCase("Active")) {
                meals = singleUserMeals();
                meals = removeOffAndAddExtras(meals, user.getId());
            } else {
                meals = 0;
            }
            userInfo.put("meals", meals);

            double mealCost;
            if (user.getStatus().getStatus().equalsIgnoreCase("Active")) {
                mealCost = meals * mealRate;
            } else {
                mealCost = 0.0;
            }
            userInfo.put("meal_cost", format(mealCost, "0.00"));

            double singleBorderOtherCost = (otherCosts / users.size());
            userInfo.put("other_cost", format(singleBorderOtherCost, "0.00"));

            mealCost += singleBorderOtherCost;
            userInfo.put("total_cost", format(mealCost, "0.00"));

            long deposit = incomeService.getSumOfAmountByUserAndType(user.getId(), depositType.getId());
            userInfo.put("deposit", deposit);

            var balance = deposit - mealCost;
            userInfo.put("balance", format(balance, "0.00"));

            var infos = (List<Object>) report.get("borders");
            infos.add(userInfo);
            report.put("borders", infos);
        }
    }

    private double calculateMealRate(Map<String, Object> report) {
        TransactionType marketType = transactionTypeService.getByTitle("Market");
        if (marketType == null || marketType.getId() == 0) return 0.0;

        long totalMarketCost = costService.getSumByType(marketType.getId());
        long totalMeals = calculateTotalMeals(report);

        double mealRate = totalMarketCost / Double.parseDouble(String.valueOf(totalMeals));
        return format(mealRate, "0.00");
    }

    private long calculateTotalMeals(Map<String, Object> report) {

        long singlePersonMeals = singleUserMeals();

        var activeStatus = userStatusService.getByStatus("Active");
        long totalUsers = userService.getTotalUsers(activeStatus.getId());
        report.put("total_borders",totalUsers);

        var totalMeals = singlePersonMeals * totalUsers;
        totalMeals =  removeOffAndAddExtras(totalMeals, 0);
        report.put("total_meals", totalMeals);
        return totalMeals;
    }

    private long singleUserMeals() {
        long periods = periodService.count();
        return totalsDays() * periods;
    }

    private long totalsDays() {
        LocalDate currentDate = LocalDate.now();
        LocalDate firstDayOfMonth = LocalDate.of(currentDate.getYear(), currentDate.getMonth(), 1);
        Period until = firstDayOfMonth.until(currentDate);

        return until.getDays();
    }

    private long removeOffAndAddExtras(long singlePersonMeals, long user) {

        List<MealDTO> mealList;
        if (user == 0) {
            mealList = mealService.getAll();
        } else {
            mealList = mealService.getAllByUser(user);
        }

        for (MealDTO meal : mealList) {
            if (meal.type().getName().equalsIgnoreCase("off")) {
                singlePersonMeals -= meal.amount();
            } else if (meal.type().getName().equalsIgnoreCase("extra")) {
                singlePersonMeals += meal.amount();
            }
        }

        return singlePersonMeals;
    }

    private double format(double actual, String format) {
        var decimalFormat = new DecimalFormat(format);
        return Double.parseDouble(decimalFormat.format(actual));
    }
}
