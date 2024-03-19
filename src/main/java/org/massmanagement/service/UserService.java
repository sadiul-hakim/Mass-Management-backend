package org.massmanagement.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.massmanagement.dto.UserDTO;
import org.massmanagement.model.User;
import org.massmanagement.repository.UserRepo;
import org.massmanagement.repository.UserRoleRepo;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepo userRepo;
    private final UserRoleRepo userRoleRepo;
    private final UserRoleService userRoleService;
    private final UserStatusService userStatusService;

    public UserDTO save(User user) {
        log.info("Saving user : {}", user);
        var saved = userRepo.save(user);
        return convertToDTO(saved);
    }

    public UserDTO getById(long id) {
        log.info("Getting user by id : {}", id);
        var role = userRepo.findById(id).orElse(new User());
        return convertToDTO(role);
    }

    public List<UserDTO> getByRole(long roleId) {
        log.info("Getting users by id : {}", roleId);
        var role = userRoleRepo.findById(roleId).orElse(null);
        if(role == null) return Collections.emptyList();

        var users= userRepo.findByRole(role);
        return users.stream().map(this::convertToDTO).toList();
    }

    public List<UserDTO> getAll() {
        log.info("Getting all users.");
        var all = userRepo.findAll();
        return all.stream().map(this::convertToDTO).toList();
    }

    public long getTotalUsers(){
        log.info("Getting total number of user.");
        return userRepo.findCountOfUser();
    }

    public List<Long> idList(){
        log.info("Getting name list of user.");
        return userRepo.findAllNames();
    }

    @Transactional
    public boolean changeManager(long managerId, long userId) {
        try{
            var user = userRepo.findById(userId);
            if(user.isEmpty()) return false;

            var manager = userRepo.findById(managerId);
            if(manager.isEmpty()) return false;

            manager.get().setRole(user.get().getRole());
            user.get().setRole(manager.get().getRole());

            return true;
        }catch (Exception ex){
            return false;
        }
    }

    public boolean delete(long id) {
        log.info("Deleting user by id : {}", id);
        try {
            userRepo.deleteById(id);
            log.info("User {} deleted successfully.", id);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public UserDTO convertToDTO(User user) {
        if (user == null) return null;

        var status = userStatusService.getById(user.getStatus());

        return new UserDTO(
                user.getId(),
                user.getName(),
                user.getPhone(),
                user.getAddress(),
                userRoleService.convertToDTO(user.getRole()),
                status,
                user.getJoiningDate()
        );
    }
}
