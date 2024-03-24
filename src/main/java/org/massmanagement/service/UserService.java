package org.massmanagement.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.massmanagement.dto.UserDTO;
import org.massmanagement.model.User;
import org.massmanagement.model.UserRole;
import org.massmanagement.repository.UserRepo;
import org.massmanagement.repository.UserRoleRepo;
import org.massmanagement.util.DateFormatter;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;

    public UserDTO save(User user) {
        log.info("Saving user : {}", user);
        if (user.getRole() == null) {
            return null;
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

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
        if (role == null) return Collections.emptyList();

        var users = userRepo.findByRole(role);
        return users.stream().map(this::convertToDTO).toList();
    }

    public List<UserDTO> getAll() {
        log.info("Getting all users.");
        var all = userRepo.findAll();
        return all.stream().map(this::convertToDTO).toList();
    }

    public long getTotalUsers(long status) {
        log.info("Getting total number of user.");
        return userRepo.findCountOfActiveUser(status);
    }

    public List<Long> idList() {
        log.info("Getting name list of user.");
        return userRepo.findAllNames();
    }

    @Transactional
    public boolean changeManager(long managerId, long userId) {
        try {
            var user = userRepo.findById(userId);
            if (user.isEmpty()) return false;

            var manager = userRepo.findById(managerId);
            if (manager.isEmpty()) return false;

            UserRole managerRole = manager.get().getRole();
            UserRole borderRole = user.get().getRole();

            manager.get().setRole(borderRole);
            user.get().setRole(managerRole);

            return true;
        } catch (Exception ex) {
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
        if (user == null || user.getId() == 0) return null;

        var status = userStatusService.getById(user.getStatus());

        return new UserDTO(
                user.getId(),
                user.getName(),
                user.getPhone(),
                user.getEmail(),
                user.getAddress(),
                userRoleService.convertToDTO(user.getRole()),
                status,
                DateFormatter.formatDateTime(user.getJoiningDate())
        );
    }
}
