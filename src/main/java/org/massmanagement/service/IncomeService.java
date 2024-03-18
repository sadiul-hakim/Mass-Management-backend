package org.massmanagement.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.massmanagement.model.Income;
import org.massmanagement.repository.IncomeRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class IncomeService {
    private final IncomeRepo incomeRepo;
    public Income save(Income income) {
        log.info("Saving income : {}", income);
        return incomeRepo.save(income);
    }

    public Income getById(long id) {
        log.info("Getting income by id : {}", id);
        return incomeRepo.findById(id).orElse(new Income());
    }

    public List<Income> getAll() {
        log.info("Getting all incomes.");
        return incomeRepo.findAll();
    }

    public List<Income> getAllByType(long type){
        log.info("Getting all incomes by type {}.",type);
        return incomeRepo.findAllByType(type);
    }

    public List<Income> getAllByUser(long user){
        log.info("Getting all incomes by user {}.",user);
        return incomeRepo.findAllByUserId(user);
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
}
