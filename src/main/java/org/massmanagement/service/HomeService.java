package org.massmanagement.service;

import org.massmanagement.dto.IncomeDTO;
import org.massmanagement.dto.UserDTO;
import org.massmanagement.model.Setting;
import org.massmanagement.model.UserStatus;
import org.massmanagement.projection.UserProjection;
import org.massmanagement.util.SettingParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class HomeService {
    private final IncomeService incomeService;
    private final CostService costService;
    private final UserService userService;
    private final TransactionTypeService transactionTypeService;
    private final UserStatusService userStatusService;
    private final SettingService settingService;

    private static final Logger log = LoggerFactory.getLogger(HomeService.class);

    public HomeService(IncomeService incomeService, CostService costService, UserService userService,
                       TransactionTypeService transactionTypeService, UserStatusService userStatusService, SettingService settingService) {
        this.incomeService = incomeService;
        this.costService = costService;
        this.userService = userService;
        this.transactionTypeService = transactionTypeService;
        this.userStatusService = userStatusService;
        this.settingService = settingService;
    }

    public Map<String, Long> getTotals() {

        log.info("Getting totals.");

        try {
            Setting setting = settingService.getByName(SettingParameter.ENTRY_NAME);
            UserStatus active = userStatusService.getById(setting.getPropertyLong(SettingParameter.USER_STATUS_ACTIVE));

            var activeStatus = userStatusService.getByStatus(active.getStatus());

            Map<String, Long> totals = new HashMap<>();
            long deposit = incomeService.getSumByType(setting.getPropertyLong(SettingParameter.TRANSACTION_TYPE_BORDER_DEPOSIT));
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
        } catch (Exception ex) {
            return Collections.emptyMap();
        }
    }

    public List<Map<String, Object>> borderInformation() {

        log.info("Getting User info.");

        try {
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
                long depositType = setting.getPropertyLong(SettingParameter.TRANSACTION_TYPE_BORDER_DEPOSIT);
                var deposit = transactionTypeService.getById(depositType);
                long sumOfDeposit = incomeService.getSumOfAmountByUserAndType(user.getId(), depositType);

                if (deposit.getId() != 0) {
                    info.put("Deposit", sumOfDeposit);
                }

                borderInfo.add(info);
            }

            return borderInfo;
        } catch (Exception ex) {
            return new ArrayList<>();
        }
    }
}
