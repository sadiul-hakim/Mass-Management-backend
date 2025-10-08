package org.massmanagement.service;

import org.massmanagement.model.UserStatus;
import org.massmanagement.repository.UserRepo;
import org.massmanagement.repository.UserStatusRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserStatusService {
    private final UserStatusRepo userStatusRepo;
    private final UserRepo userRepo;

    private static final Logger log = LoggerFactory.getLogger(UserStatusService.class);

    public UserStatusService(UserStatusRepo userStatusRepo, UserRepo userRepo) {
        this.userStatusRepo = userStatusRepo;
        this.userRepo = userRepo;
    }

    public UserStatus save(UserStatus userStatus) {
        log.info("Saving user role : {}", userStatus);

        if (userStatus.getStatus().isEmpty()) {
            log.warn("Invalid User Status {}", userStatus);
            return new UserStatus();
        }

        UserStatus status = getByStatus(userStatus.getStatus());
        if(status.getId() != 0){
            log.warn("Status already exists!");
            return new UserStatus();
        }

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

            if (!userRepo.findAllByStatus(id).isEmpty()) {
                log.warn("User Status is in use!");
                return false;
            }

            userStatusRepo.deleteById(id);
            log.info("User status {} deleted by successfully.", id);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}
