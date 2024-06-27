package org.massmanagement.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.massmanagement.model.TransactionType;
import org.massmanagement.repository.CostRepo;
import org.massmanagement.repository.IncomeRepo;
import org.massmanagement.repository.TransactionTypeRepo;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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

        if (transactionType.getTitle().isEmpty()) {
            log.warn("Invalid type!");
            log.info(transactionType.toString());
            return new TransactionType();
        }

        clearCache();
        return transactionTypeRepo.save(transactionType);
    }

    @Cacheable("TransactionType:getById")
    public TransactionType getById(long id) {
        log.info("Getting transaction type by id : {}", id);
        return transactionTypeRepo.findById(id).orElse(new TransactionType());
    }

    @Cacheable("TransactionType:getByTitle")
    public TransactionType getByTitle(String title) {
        log.info("Getting transaction type by name : {}", title);
        return transactionTypeRepo.findByTitle(title).orElse(new TransactionType());
    }

    @Cacheable("TransactionType:getAll")
    public List<TransactionType> getAll() {
        log.info("Getting all transaction types.");
        return transactionTypeRepo.findAll();
    }

    public boolean delete(long id) {
        log.info("Deleting transaction type id : {}", id);
        try {

            if (!(incomeRepo.findAllByType(id).isEmpty() && costRepo.findAllByType(id).isEmpty())) {
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

    @CacheEvict(value = {"TransactionType:getById", "TransactionType:getByTitle", "TransactionType:getAll"}, allEntries = true)
    public void clearCache() {
        log.info("Cleared all Transaction Type Cache!");
    }
}
