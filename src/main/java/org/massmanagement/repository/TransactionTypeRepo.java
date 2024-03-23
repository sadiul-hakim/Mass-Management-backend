package org.massmanagement.repository;

import org.massmanagement.model.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TransactionTypeRepo extends JpaRepository<TransactionType,Long> {
    Optional<TransactionType> findByTitle(String title);
}
