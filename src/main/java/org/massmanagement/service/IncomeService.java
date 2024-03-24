package org.massmanagement.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.massmanagement.dto.IncomeDTO;
import org.massmanagement.model.Income;
import org.massmanagement.model.TransactionType;
import org.massmanagement.repository.IncomeRepo;
import org.massmanagement.repository.UserRepo;
import org.massmanagement.util.DateFormatter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class IncomeService {
    private final IncomeRepo incomeRepo;
    private final TransactionTypeService transactionTypeService;
    private final UserService userService;
    public IncomeDTO save(Income income) {
        log.info("Saving income : {}", income);
        return convertToDTO(incomeRepo.save(income));
    }

    public IncomeDTO getById(long id) {
        log.info("Getting income by id : {}", id);
        return convertToDTO(incomeRepo.findById(id).orElse(new Income()));
    }

    public List<IncomeDTO> getAll() {
        log.info("Getting all incomes.");
        var all = incomeRepo.findAll();
        return all.stream().map(this::convertToDTO).toList();
    }

    public List<IncomeDTO> getAllByType(long type){
        log.info("Getting all incomes by type {}.",type);
        var all = incomeRepo.findAllByType(type);
        return all.stream().map(this::convertToDTO).toList();
    }

    public List<IncomeDTO> getAllByUser(long user){
        log.info("Getting all incomes by user {}.",user);
        var all = incomeRepo.findAllByUserId(user);
        return all.stream().map(this::convertToDTO).toList();
    }

    public long getTotalAmount(){
        try{
            log.info("Getting total amount of income.");
            return incomeRepo.findSumOfAmount();
        }catch (Exception ex){
            log.error("Error occurred in getTotalAmount() : cause {}",ex.getMessage());
            return 0L;
        }
    }

    public Set<Long> countTypes(){
        log.info("Getting types in income.");
        return incomeRepo.findCountOfType();
    }

    public long getSumOfAmountByUserAndType(long user,long type){
        log.info("Getting sum of amount by user {} and type {}",user,type);
        return incomeRepo.findSumOfIncomeByUserIdAndType(user,type);
    }

    public boolean delete(long id) {
        log.info("Deleting income by id : {}", id);
        try {
            incomeRepo.deleteById(id);
            log.info("income {} deleted successfully.", id);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public IncomeDTO convertToDTO(Income income){
        var transactionType = transactionTypeService.getById(income.getType());
        var user = userService.getById(income.getUserId());

        return new IncomeDTO(income.getId(),transactionType,user,income.getAmount(), DateFormatter.formatDateTime(income.getDate()));
    }
}
