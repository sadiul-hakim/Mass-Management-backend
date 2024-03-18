package org.massmanagement.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.massmanagement.model.UserRole;
import org.massmanagement.repository.UserRoleRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserRoleService {
    private final UserRoleRepo userRoleRepo;
    public UserRole save(UserRole userRole) {
        log.info("Saving user role : {}", userRole);
        return userRoleRepo.save(userRole);
    }

    public UserRole getById(long id) {
        log.info("Getting user role by id : {}", id);
        return userRoleRepo.findById(id).orElse(new UserRole());
    }

    public List<UserRole> getAll() {
        log.info("Getting all user roles.");
        return userRoleRepo.findAll();
    }

    public boolean delete(long id) {
        log.info("Deleting user role by id : {}", id);
        try {
            userRoleRepo.deleteById(id);
            log.info("User role {} deleted successfully.", id);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}
