package org.massmanagement.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.massmanagement.model.UserStatus;
import org.massmanagement.repository.UserRepo;
import org.massmanagement.repository.UserStatusRepo;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserStatusService {
    private final UserStatusRepo userStatusRepo;
    private final UserRepo userRepo;

    public UserStatus save(UserStatus userStatus) {
        log.info("Saving user role : {}", userStatus);

        if (userStatus.getStatus().isEmpty()) {
            log.warn("Invalid User Status {}", userStatus);
            return null;
        }

        UserStatus status = getByStatus(userStatus.getStatus());
        if(status.getId() != 0){
            log.warn("Status already exists!");
            return null;
        }

        clearCache();
        return userStatusRepo.save(userStatus);
    }

    @Cacheable("UserStatus:getById")
    public UserStatus getById(long id) {
        log.info("Getting user status by id : {}", id);
        return userStatusRepo.findById(id).orElse(new UserStatus());
    }

    public UserStatus getByStatus(String status) {
        log.info("Getting user status by status name : {}", status);
        return userStatusRepo.findByStatus(status).orElse(new UserStatus());
    }

    @Cacheable("UserStatus:getAll")
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

    @CacheEvict(value = {"UserStatus:getById", "UserStatus:getAll"}, allEntries = true)
    public void clearCache() {
        log.info("Cleared all User Status Cache!");
    }
}
