package org.massmanagement.service;

import jakarta.transaction.Transactional;
import org.massmanagement.dto.ChangePasswordDTO;
import org.massmanagement.dto.UserDTO;
import org.massmanagement.dto.UserUpdateDTO;
import org.massmanagement.model.Setting;
import org.massmanagement.model.User;
import org.massmanagement.model.UserRole;
import org.massmanagement.projection.UserProjection;
import org.massmanagement.repository.UserRepo;
import org.massmanagement.repository.UserRoleRepo;
import org.massmanagement.util.DateFormatter;
import org.massmanagement.util.SettingParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class UserService {
    private final UserRepo userRepo;
    private final UserRoleRepo userRoleRepo;
    private final UserRoleService userRoleService;
    private final UserStatusService userStatusService;
    private final PasswordEncoder passwordEncoder;
    private final SettingService settingService;

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    public UserService(UserRepo userRepo, UserRoleRepo userRoleRepo, UserRoleService userRoleService, UserStatusService userStatusService, PasswordEncoder passwordEncoder, SettingService settingService) {
        this.userRepo = userRepo;
        this.userRoleRepo = userRoleRepo;
        this.userRoleService = userRoleService;
        this.userStatusService = userStatusService;
        this.passwordEncoder = passwordEncoder;
        this.settingService = settingService;
    }

    public UserDTO save(User user) {
        log.info("Saving user : {}", user);

        // User Validation
        if (user.getRole() == null || user.getName().isEmpty() ||
                user.getPhone().isEmpty() || user.getEmail().isEmpty()
                || user.getPassword().isEmpty() || user.getStatus() == 0) {
            log.warn("Invalid user {}", user);
            return new UserDTO();
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        var saved = userRepo.save(user);
        return convertToDTO(saved);
    }

    public boolean update(UserUpdateDTO user) {
        log.info("Updating user : {}", user);

        User model = getModelById(user.id());
        if (model == null) {
            log.warn("User not found with id: {}", user.id());
            return false;
        }

        if (!user.name().isEmpty()) {
            model.setName(user.name());
        }
        if (!user.email().isEmpty()) {
            model.setEmail(user.email());
        }
        if (!user.phone().isEmpty()) {
            model.setPhone(user.phone());
        }
        if (!user.address().isEmpty()) {
            model.setAddress(user.address());
        }
        if (user.status() != 0) {
            model.setStatus(user.status());
        }
        save(model);
        return true;
    }

    public UserDTO getById(long id) {
        log.info("Getting user by id : {}", id);
        var role = userRepo.findById(id).orElse(new User());
        return convertToDTO(role);
    }

    public User getModelById(long id) {
        log.info("Getting user by id : {}", id);
        return userRepo.findById(id).orElse(new User());
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

    public List<UserProjection> getAllProjected() {
        log.info("Getting all users projection.");
        return userRepo.findUserProjectedBy();
    }

    public long countByStatus(long status) {
        log.info("Getting total number of user.");
        return userRepo.findCountOfActiveUser(status);
    }

    public List<UserDTO> findByStatusNotIn(List<Long> types) {
        log.info("Getting all cost where types are not in {}.", types);
        var allUsers = userRepo.findAllByStatusNotIn(types);
        return allUsers.stream().map(this::convertToDTO).toList();
    }

    public List<User> getAllModelByRole(long role) {
        log.info("Getting total number of user by role.");

        var userRole = userRoleRepo.findById(role).orElse(null);
        if (userRole == null) return Collections.emptyList();

        return userRepo.findAllByRole(userRole);
    }

    public List<User> getAllByStatus(long status) {
        log.info("Getting total number of user by status.");
        return userRepo.findAllByStatus(status);
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

            Setting setting = settingService.getByName(SettingParameter.ENTRY_NAME);
            long activeUser = setting.getPropertyLong(SettingParameter.USER_STATUS_ACTIVE);
            if (user.get().getStatus() != activeUser) {
                throw new RuntimeException("The user you are trying to make Manager is Inactive.");
            }

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
        if (user == null || user.getId() == 0) return new UserDTO();

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

    public boolean changePassword(ChangePasswordDTO dto) {

        if (!dto.newPassword().equals(dto.confirmPassword())) {
            return false;
        }

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated() || (authentication instanceof AnonymousAuthenticationToken)) {
                return false;
            }

            UserDetails principal = (UserDetails) authentication.getPrincipal();
            boolean matches = passwordEncoder.matches(dto.currentPassword(), principal.getPassword());
            if (!matches) {
                return false;
            }

            User user = userRepo.findByEmail(principal.getUsername()).get();
            user.setPassword(dto.newPassword());
            save(user);

            return true;
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return false;
        }
    }
}
