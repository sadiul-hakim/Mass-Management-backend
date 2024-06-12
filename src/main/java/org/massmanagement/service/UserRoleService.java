package org.massmanagement.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.massmanagement.dto.RoleDTO;
import org.massmanagement.model.UserRole;
import org.massmanagement.repository.UserRepo;
import org.massmanagement.repository.UserRoleRepo;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserRoleService {
    private final UserRoleRepo userRoleRepo;
    private final UserRepo userRepo;

    public RoleDTO save(UserRole userRole) {
        log.info("Saving user role : {}", userRole);

        if (userRole == null || userRole.getRole().isEmpty()) {
            return null;
        }

        RoleDTO role = getByRole("ROLE_" + userRole.getRole().toUpperCase());
        if (role.id() != 0) {
            log.warn("Role already exists!");
            return null;
        }

        userRole.setRole(userRole.getRole().toUpperCase());

        if (!userRole.getRole().startsWith("ROLE_")) {
            userRole.setRole("ROLE_".concat(userRole.getRole()));
        }

        var saved = userRoleRepo.save(userRole);

        clearCache();
        return convertToDTO(saved);
    }

    @Cacheable("UseRole:getById")
    public RoleDTO getById(long id) {
        log.info("Getting user role by id : {}", id);
        var role = userRoleRepo.findById(id).orElse(new UserRole());
        return convertToDTO(role);
    }

    @Cacheable("UseRole:getByRole")
    public RoleDTO getByRole(String role) {
        log.info("Getting user role by role name : {}", role);

        if (role.isEmpty()) return new RoleDTO();

        role = role.toUpperCase();

        if (!role.startsWith("ROLE_")) {
            role = "ROLE_".concat(role);
        }

        var roleModel = userRoleRepo.findByRole(role).orElse(new UserRole());
        return convertToDTO(roleModel);
    }

    @Cacheable("UseRole:getAll")
    public List<RoleDTO> getAll() {
        log.info("Getting all user roles.");
        var all = userRoleRepo.findAll();
        return all.stream().map(this::convertToDTO).toList();
    }

    public boolean delete(long id) {
        log.info("Deleting user role by id : {}", id);
        try {

            var role = userRoleRepo.findById(id).orElse(null);

            if (!userRepo.findAllByRole(role).isEmpty()) {
                log.warn("User Role is in use!");
                return false;
            }

            userRoleRepo.deleteById(id);
            log.info("User role {} deleted successfully.", id);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public RoleDTO convertToDTO(UserRole userRole) {
        if (userRole == null) return new RoleDTO();
        return new RoleDTO(userRole.getId(), userRole.getRole().replace("ROLE_", ""), userRole.getDescription());
    }

    @CacheEvict(value = {"UseRole:getById", "UseRole:getByRole", "UseRole:getAll"}, allEntries = true)
    public void clearCache() {
        log.info("Cleared all User Role Cache!");
    }
}
