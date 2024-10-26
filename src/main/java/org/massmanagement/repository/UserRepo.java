package org.massmanagement.repository;

import org.massmanagement.model.User;
import org.massmanagement.model.UserRole;
import org.massmanagement.projection.UserProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepo extends JpaRepository<User,Long> {
    List<User> findByRole(UserRole role);
    @Query(value = "SELECT COUNT(*) FROM user where status = :status",nativeQuery = true)
    long findCountOfActiveUser(@Param("status") long status);
    @Query(value = "select id from USER",nativeQuery = true)
    List<Long> findAllNames();
    Optional<User> findByEmail(String email);
    List<UserProjection> findUserProjectedBy();
    List<User> findAllByRole(UserRole role);
    List<User> findAllByStatus(long status);
    List<User> findAllByStatusNotIn(List<Long> status);
}
