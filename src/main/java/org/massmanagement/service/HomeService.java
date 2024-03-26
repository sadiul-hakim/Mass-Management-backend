package org.massmanagement.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.massmanagement.dto.UserDTO;
import org.massmanagement.projection.UserProjection;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class HomeService {
    private final IncomeService incomeService;
    private final CostService costService;
    private final UserService userService;
    private final TransactionTypeService transactionTypeService;
    private final UserStatusService userStatusService;

    public Map<String, Long> getTotals() {

        var activeStatus = userStatusService.getByStatus("Active");
        var inactiveStatus = userStatusService.getByStatus("Inactive");

        Map<String, Long> totals = new HashMap<>();
        long income = incomeService.getTotalAmount();
        long cost = costService.getTotalAmount();
        long activeUsers = userService.getTotalUsers(activeStatus.getId());
        long inactiveUsers = userService.getTotalUsers(inactiveStatus.getId());

        totals.put("income", income);
        totals.put("cost", cost);
        totals.put("active_user", activeUsers);
        totals.put("inactive_user", inactiveUsers);
        return totals;
    }

    public List<Map<String, Object>> borderInformation() {

        List<Map<String, Object>> borderInfo = new ArrayList<>();

        List<UserProjection> userIds = userService.getAllProjected();
        Set<Long> types = incomeService.countTypes();

        for (UserProjection user : userIds) {
            Map<String, Object> info = new HashMap<>();
            info.put("name",user.getName());

            for (long type : types) {
                var transactionType = transactionTypeService.getById(type);
                long sum = incomeService.getSumOfAmountByUserAndType(user.getId(),type);
                info.put(transactionType.getTitle(),sum);
            }

            borderInfo.add(info);
        }

        return borderInfo;
    }
}
