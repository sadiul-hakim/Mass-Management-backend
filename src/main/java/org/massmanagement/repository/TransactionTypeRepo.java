package org.massmanagement.repository;

import org.massmanagement.model.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionTypeRepo extends JpaRepository<TransactionType,Long> {
}
