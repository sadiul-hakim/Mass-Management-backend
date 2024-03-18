package org.massmanagement.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.massmanagement.model.UserStatus;
import org.massmanagement.repository.UserStatusRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserStatusService {
    private final UserStatusRepo userStatusRepo;
    public UserStatus save(UserStatus userStatus) {
        log.info("Saving user role : {}", userStatus);
        return userStatusRepo.save(userStatus);
    }

    public UserStatus getById(long id) {
        log.info("Getting user status by id : {}", id);
        return userStatusRepo.findById(id).orElse(new UserStatus());
    }

    public UserStatus getByStatus(String status) {
        log.info("Getting user status by status name : {}", status);
        return userStatusRepo.findByStatus(status).orElse(new UserStatus());
    }

    public List<UserStatus> getAll() {
        log.info("Getting all user status.");
        return userStatusRepo.findAll();
    }

    public boolean delete(long id) {
        log.info("Deleting user status by id : {}", id);
        try {
            userStatusRepo.deleteById(id);
            log.info("User status {} deleted by successfully.", id);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}
