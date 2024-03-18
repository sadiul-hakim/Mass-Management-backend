package org.massmanagement.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.massmanagement.model.TransactionType;
import org.massmanagement.repository.TransactionTypeRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionTypeService {
    private final TransactionTypeRepo transactionTypeRepo;

    public TransactionType save(TransactionType transactionType) {
        log.info("Saving Transaction Type : {}", transactionType);
        return transactionTypeRepo.save(transactionType);
    }

    public TransactionType getById(long id) {
        log.info("Getting transaction type by id : {}", id);
        return transactionTypeRepo.findById(id).orElse(new TransactionType());
    }

    public List<TransactionType> getAll() {
        log.info("Getting all transaction types.");
        return transactionTypeRepo.findAll();
    }

    public boolean delete(long id) {
        log.info("Deleting transaction type id : {}", id);
        try {
            transactionTypeRepo.deleteById(id);
            log.info("transaction type {} deleted successfully.", id);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}
