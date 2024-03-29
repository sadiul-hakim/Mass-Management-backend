package org.massmanagement.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.massmanagement.model.TransactionType;
import org.massmanagement.repository.CostRepo;
import org.massmanagement.repository.IncomeRepo;
import org.massmanagement.repository.TransactionTypeRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionTypeService {
    private final TransactionTypeRepo transactionTypeRepo;
    private final IncomeRepo incomeRepo;
    private final CostRepo costRepo;

    public TransactionType save(TransactionType transactionType) {
        log.info("Saving Transaction Type : {}", transactionType);
        return transactionTypeRepo.save(transactionType);
    }

    public TransactionType getById(long id) {
        log.info("Getting transaction type by id : {}", id);
        return transactionTypeRepo.findById(id).orElse(new TransactionType());
    }

    public TransactionType getByTitle(String title) {
        log.info("Getting transaction type by name : {}", title);
        return transactionTypeRepo.findByTitle(title).orElse(new TransactionType());
    }

    public List<TransactionType> getAll() {
        log.info("Getting all transaction types.");
        return transactionTypeRepo.findAll();
    }

    public boolean delete(long id) {
        log.info("Deleting transaction type id : {}", id);
        try {

            if(!(incomeRepo.findAllByType(id).isEmpty() && costRepo.findAllByType(id).isEmpty())){
                log.warn("Transaction type is in use!");
                return false;
            }

            transactionTypeRepo.deleteById(id);
            log.info("transaction type {} deleted successfully.", id);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}
