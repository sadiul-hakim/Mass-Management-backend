package org.massmanagement.repository;

import org.massmanagement.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRoleRepo extends JpaRepository<UserRole,Long> {
    Optional<UserRole> findByRole(String role);
}
