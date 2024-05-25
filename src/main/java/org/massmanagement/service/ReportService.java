package org.massmanagement.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.massmanagement.dto.CostDTO;
import org.massmanagement.dto.MealDTO;
import org.massmanagement.dto.UserDTO;
import org.massmanagement.model.Setting;
import org.massmanagement.util.DateFormatter;
import org.massmanagement.util.SettingParameter;
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
    private final PeriodService periodService;
    private final MealService mealService;
    private final UserService userService;
    private final IncomeService incomeService;
    private final SettingService settingService;

    public Map<String, Object> generateReport() {
        log.info("Generating report.");

        Setting setting = settingService.getByName(SettingParameter.ENTRY_NAME);
        if (settingService.isInvalid(setting)) {
            log.info("Setting is not configured properly!");
            return Collections.emptyMap();
        }

        Map<String, Object> report = new HashMap<>();

        var mealRate = calculateMealRate(report, setting);
        report.put("mealRate", mealRate);

        long income = incomeService.getTotalAmount();
        long cost = costService.getTotalAmount();
        report.put("total_income", income);
        report.put("total_cost", cost);

        long market = costService.getSumByType(setting.getProperty(SettingParameter.TRANSACTION_TYPE_MARKET));
        report.put("market", market);

        long deposit = incomeService.getSumByType(setting.getProperty(SettingParameter.TRANSACTION_TYPE_BORDER_DEPOSIT));
        report.put("deposit", deposit);

        var users = userService.getAll();
        if (users.isEmpty()) return Collections.emptyMap();

        loadUserInfo(mealRate, report, users, setting);

        return report;
    }

    private void loadUserInfo(double mealRate, Map<String, Object> report, List<UserDTO> users, Setting setting) {

        report.put("borders", new ArrayList<>());

        var otherCostsObj = costService.findByTypeNotIn(setting.getExcludeTransactionTypes());

        double otherCosts = 0.0;
        for (CostDTO cost : otherCostsObj) {
            otherCosts += cost.amount();
        }
        report.put("extra_cost", format(otherCosts, "0.00"));

        var date = LocalDate.now();
        report.put("date", DateFormatter.formatDate(date));

        for (UserDTO user : users) {

            Map<String, Object> userInfo = new HashMap<>();

            Map<String, Object> userData = new HashMap<>();
            userData.put("name", user.name());
            userData.put("id", user.id());

            userInfo.put("border", userData);

            long meals;
            if (user.status().getStatus().equalsIgnoreCase("Active")) {
                meals = singleUserMeals();
                meals = removeOffAndAddExtras(meals, user.id(), setting);
            } else {
                meals = 0;
            }
            userInfo.put("meals", meals);

            double mealCost;
            if (user.status().getStatus().equalsIgnoreCase("Active")) {
                mealCost = meals * mealRate;
            } else {
                mealCost = 0.0;
            }
            userInfo.put("meal_cost", format(mealCost, "0.00"));

            double singleBorderOtherCost = (otherCosts / users.size());
            userInfo.put("extra_cost", format(singleBorderOtherCost, "0.00"));

            mealCost += singleBorderOtherCost;
            userInfo.put("total_cost", format(mealCost, "0.00"));

            long deposit = incomeService.getSumOfAmountByUserAndType(user.id(), setting.getProperty(SettingParameter.TRANSACTION_TYPE_BORDER_DEPOSIT));
            userInfo.put("deposit", deposit);

            var balance = deposit - mealCost;
            userInfo.put("balance", format(balance, "0.00"));

            var infos = (List<Object>) report.get("borders");
            infos.add(userInfo);
            report.put("borders", infos);
        }
    }

    private double calculateMealRate(Map<String, Object> report, Setting setting) {

        long marketTypeId = setting.getProperty(SettingParameter.TRANSACTION_TYPE_MARKET);
        if (marketTypeId == 0) return 0.0;

        long totalMarketCost = costService.getSumByType(marketTypeId);
        long totalMeals = calculateTotalMeals(report, setting);

        double mealRate = totalMarketCost / Double.parseDouble(String.valueOf(totalMeals));
        return format(mealRate, "0.00");
    }

    private long calculateTotalMeals(Map<String, Object> report, Setting setting) {

        long singlePersonMeals = singleUserMeals();

        long totalUsers = userService.countByStatus(setting.getProperty(SettingParameter.USER_STATUS_ACTIVE));
        report.put("total_borders", totalUsers);

        var totalMeals = singlePersonMeals * totalUsers;
        totalMeals = removeOffAndAddExtras(totalMeals, 0, setting);
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

        return until.getDays() + 1;
    }

    private long removeOffAndAddExtras(long singlePersonMeals, long user, Setting setting) {


        List<MealDTO> mealList;
        if (user == 0) {
            mealList = mealService.getAll();
        } else {
            mealList = mealService.getAllByUser(user);
        }

        for (MealDTO meal : mealList) {

            LocalDate today = LocalDate.now();
            LocalDate mealDate = DateFormatter.stringToLocalDate(meal.date());
            if (mealDate.isAfter(today)) {
                continue;
            }

            if (meal.type().getId() == setting.getProperty(SettingParameter.MEAL_TYPE_OFF)) {
                singlePersonMeals -= meal.amount();
            } else if (meal.type().getId() == setting.getProperty(SettingParameter.MEAL_TYPE_EXTRA)) {
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
