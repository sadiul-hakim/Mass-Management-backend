package org.massmanagement.repository;

import org.massmanagement.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepo extends JpaRepository<UserRole,Long> {
}
