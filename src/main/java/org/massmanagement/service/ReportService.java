package org.massmanagement.service;

import org.massmanagement.dto.CostDTO;
import org.massmanagement.dto.MealDTO;
import org.massmanagement.dto.UserDTO;
import org.massmanagement.model.Period;
import org.massmanagement.model.Setting;
import org.massmanagement.util.DateFormatter;
import org.massmanagement.util.SettingParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.*;

@Service
public class ReportService {
    private final CostService costService;
    private final PeriodService periodService;
    private final MealService mealService;
    private final UserService userService;
    private final IncomeService incomeService;
    private final SettingService settingService;

    private static final Logger log = LoggerFactory.getLogger(ReportService.class);

    public ReportService(CostService costService, PeriodService periodService, MealService mealService,
                         UserService userService, IncomeService incomeService, SettingService settingService) {
        this.costService = costService;
        this.periodService = periodService;
        this.mealService = mealService;
        this.userService = userService;
        this.incomeService = incomeService;
        this.settingService = settingService;
    }

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

        long market = costService.getSumByType(setting.getPropertyLong(SettingParameter.TRANSACTION_TYPE_MARKET));
        report.put("market", market);

        long deposit = incomeService.getSumByType(setting.getPropertyLong(SettingParameter.TRANSACTION_TYPE_BORDER_DEPOSIT));
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

            double meals;
            long fixedMeal = setting.getPropertyLong(SettingParameter.NUMBER_OF_FIXED_MEAL);
            if (user.status().getStatus().equalsIgnoreCase("Active")) {
                meals = singleUserMeals();
                meals = removeOffAndAddExtras(meals, user.id(), setting);

                if (fixedMeal > meals) {
                    double extraMeal = fixedMeal - meals;
                    report.put("total_meals", ((double) report.get("total_meals")) + extraMeal);
                }
            } else {
                meals = 0;
            }

            userInfo.put("meals", meals);
            meals = Math.max(meals, fixedMeal);
            userInfo.put("chargeable_meals", meals);

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

            long deposit = incomeService.getSumOfAmountByUserAndType(user.id(), setting.getPropertyLong(SettingParameter.TRANSACTION_TYPE_BORDER_DEPOSIT));
            userInfo.put("deposit", deposit);

            var balance = deposit - mealCost;
            userInfo.put("balance", format(balance, "0.00"));

            var infos = (List<Object>) report.get("borders");
            infos.add(userInfo);
            report.put("borders", infos);
        }
    }

    private double calculateMealRate(Map<String, Object> report, Setting setting) {

        long marketTypeId = setting.getPropertyLong(SettingParameter.TRANSACTION_TYPE_MARKET);
        if (marketTypeId == 0) return 0.0;

        long totalMarketCost = costService.getSumByType(marketTypeId);
        double totalMeals = calculateTotalMeals(report, setting);

        double mealRate = totalMarketCost / totalMeals;
        return format(mealRate, "0.00");
    }

    private double calculateTotalMeals(Map<String, Object> report, Setting setting) {

        double singlePersonMeals = singleUserMeals();

        long totalUsers = userService.countByStatus(setting.getPropertyLong(SettingParameter.USER_STATUS_ACTIVE));
        report.put("total_borders", totalUsers);

        var totalMeals = singlePersonMeals * totalUsers;
        totalMeals = removeOffAndAddExtras(totalMeals, 0, setting);
        report.put("total_meals", totalMeals);
        return totalMeals;
    }

    private double singleUserMeals() {
        List<Period> periods = periodService.getAll();

        double meals = 0;
        for (Period period : periods) {
            meals += period.getMeal();
        }

        return meals * totalsDays();
    }

    private long totalsDays() {
        LocalDate currentDate = LocalDate.now();
        LocalDate firstDayOfMonth = LocalDate.of(currentDate.getYear(), currentDate.getMonth(), 1);
        java.time.Period until = firstDayOfMonth.until(currentDate);

        return until.getDays() + 1;
    }

    private double removeOffAndAddExtras(double singlePersonMeals, long user, Setting setting) {


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

            if (meal.type().getId() == setting.getPropertyLong(SettingParameter.MEAL_TYPE_OFF)) {
                singlePersonMeals -= meal.amount();
            } else if (meal.type().getId() == setting.getPropertyLong(SettingParameter.MEAL_TYPE_EXTRA)) {
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
