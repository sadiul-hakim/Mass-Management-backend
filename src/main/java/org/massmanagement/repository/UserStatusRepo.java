package org.massmanagement.repository;

import org.massmanagement.model.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserStatusRepo extends JpaRepository<UserStatus,Long> {
}
