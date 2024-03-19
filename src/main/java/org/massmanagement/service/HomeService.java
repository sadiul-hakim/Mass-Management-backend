package org.massmanagement.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.massmanagement.dto.UserDTO;
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

    public Map<String, Long> getTotals() {
        Map<String, Long> totals = new HashMap<>();
        long income = incomeService.getTotalAmount();
        long cost = costService.getTotalAmount();
        long user = userService.getTotalUsers();

        totals.put("income", income);
        totals.put("cost", cost);
        totals.put("user", user);
        return totals;
    }

    public List<Map<String, Object>> borderInformation() {

        List<Map<String, Object>> borderInfo = new ArrayList<>();

        List<UserDTO> userIds = userService.getAll();
        Set<Long> types = incomeService.countTypes();

        for (UserDTO user : userIds) {
            Map<String, Object> info = new HashMap<>();
            for (long type : types) {
                var transactionType = transactionTypeService.getById(type);
                long sum = incomeService.getSumOfAmountByUserAndType(user.getId(),type);
                info.put("name",user.getName());
                info.put(transactionType.getTitle(),sum);
            }

            borderInfo.add(info);
        }

        return borderInfo;
    }
}
