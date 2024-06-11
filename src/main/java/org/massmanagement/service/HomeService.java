package org.massmanagement.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.massmanagement.dto.IncomeDTO;
import org.massmanagement.dto.UserDTO;
import org.massmanagement.model.Setting;
import org.massmanagement.model.UserStatus;
import org.massmanagement.projection.UserProjection;
import org.massmanagement.util.SettingParameter;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class HomeService {
    private final IncomeService incomeService;
    private final CostService costService;
    private final UserService userService;
    private final TransactionTypeService transactionTypeService;
    private final UserStatusService userStatusService;
    private final SettingService settingService;

    public Map<String, Long> getTotals() {

        log.info("Getting totals.");

        Setting setting = settingService.getByName(SettingParameter.ENTRY_NAME);
        UserStatus active = userStatusService.getById(setting.getProperty(SettingParameter.USER_STATUS_ACTIVE));

        var activeStatus = userStatusService.getByStatus(active.getStatus());

        Map<String, Long> totals = new HashMap<>();
        long deposit = incomeService.getSumByType(setting.getProperty(SettingParameter.TRANSACTION_TYPE_BORDER_DEPOSIT));
        long income = incomeService.getTotalAmount();
        long cost = costService.getTotalAmount();
        long activeUsers = userService.countByStatus(activeStatus.getId());
        List<UserDTO> otherUsers = userService.findByStatusNotIn(Collections.singletonList(active.getId()));

        totals.put("income", income);
        totals.put("cost", cost);
        totals.put("active_user", activeUsers);
        totals.put("other_users", (long) otherUsers.size());
        totals.put("deposit", deposit);
        return totals;
    }

    public List<Map<String, Object>> borderInformation() {

        log.info("Getting User info.");

        List<Map<String, Object>> borderInfo = new ArrayList<>();
        Setting setting = settingService.getByName(SettingParameter.ENTRY_NAME);

        List<UserProjection> userIds = userService.getAllProjected();
        List<IncomeDTO> billsReceived = incomeService.findByTypeInList(setting.getBillTypes());

        for (UserProjection user : userIds) {
            Map<String, Object> info = new HashMap<>();

            // Put username
            info.put("name", user.getName());

            // Put bills
            Map<String, Object> bills = new HashMap<>();
            for (IncomeDTO income : billsReceived) {
                var transactionType = transactionTypeService.getById(income.type().getId());
                long sum = incomeService.getSumOfAmountByUserAndType(user.getId(), income.type().getId());
                bills.put(transactionType.getTitle(), sum);
            }
            info.put("bills", bills);

            // Put deposit in info man
            long depositType = setting.getProperty(SettingParameter.TRANSACTION_TYPE_BORDER_DEPOSIT);
            var deposit = transactionTypeService.getById(depositType);
            long sumOfDeposit = incomeService.getSumOfAmountByUserAndType(user.getId(), depositType);
            info.put(deposit.getTitle(), sumOfDeposit);

            borderInfo.add(info);
        }

        return borderInfo;
    }
}
