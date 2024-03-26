package org.massmanagement.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.massmanagement.dto.CostDTO;
import org.massmanagement.model.Cost;
import org.massmanagement.repository.CostRepo;
import org.massmanagement.util.DateFormatter;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CostService {
    private final CostRepo costRepo;
    private final TransactionTypeService transactionTypeService;

    public Cost save(Cost cost) {
        log.info("Saving cost : {}", cost);
        return costRepo.save(cost);
    }

    public Cost getById(long id) {
        log.info("Getting cost by id : {}", id);
        return costRepo.findById(id).orElse(new Cost());
    }

    public List<CostDTO> getAll() {
        log.info("Getting all cost.");
        var allCosts = costRepo.findAll();
        return allCosts.stream().map(this::convertToDTO).toList();
    }

    public List<CostDTO> getAllByType(long type) {
        log.info("Getting all cost by type {}.", type);
        var allCosts = costRepo.findAllByType(type);
        return allCosts.stream().map(this::convertToDTO).toList();
    }

    public List<CostDTO> findByTypeNotIn(List<Long> types) {
        log.info("Getting all cost where types are not in {}.", types);
        var allCosts = costRepo.findByTypeNotIn(types);
        return allCosts.stream().map(this::convertToDTO).toList();
    }

    public long getSumByType(long type) {
        try {
            log.info("Getting sum of cost by type {}.", type);
            return costRepo.findSumOfAmountByType(type);
        } catch (Exception e) {
            log.error("Error occurred in getSumByType(): cause {}", e.getMessage());
            return 0L;
        }
    }

    public long getTotalAmount() {
        try {
            log.info("Getting total amount of cost.");
            return costRepo.findSumOfAmount();
        } catch (Exception e) {
            log.error("Error occurred in getTotalAmount(): cause {}", e.getMessage());
            return 0L;
        }
    }

    public boolean delete(long id) {
        log.info("Deleting cost by id : {}", id);
        try {
            costRepo.deleteById(id);
            log.info("cost {} deleted successfully.", id);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public boolean deleteAll() {
        log.info("Deleting all costs.");
        try {
            costRepo.deleteAll();
            return true;
        } catch (Exception ex) {
            log.error("Could not delete costs. Cause {}", ex.getMessage());
            return false;
        }
    }

    public CostDTO convertToDTO(Cost cost) {
        if (cost == null) return null;
        var transactionType = transactionTypeService.getById(cost.getType());
        return new CostDTO(cost.getId(),
                transactionType,
                cost.getAmount(),
                DateFormatter.formatDateTime(cost.getDate()));
    }
}
