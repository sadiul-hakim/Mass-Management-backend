package org.massmanagement.repository;

import org.massmanagement.model.User;
import org.massmanagement.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepo extends JpaRepository<User,Long> {
    List<User> findByRole(UserRole role);
    @Query(value = "SELECT COUNT(*) FROM USER",nativeQuery = true)
    long findCountOfUser();
    @Query(value = "select id from USER",nativeQuery = true)
    List<Long> findAllNames();
}
