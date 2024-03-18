package org.massmanagement.repository;

import org.massmanagement.model.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserStatusRepo extends JpaRepository<UserStatus,Long> {
    Optional<UserStatus> findByStatus(String status);
}
