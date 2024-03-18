package org.massmanagement.repository;

import org.massmanagement.model.User;
import org.massmanagement.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepo extends JpaRepository<User,Long> {
    List<User> findByRole(UserRole role);
}
