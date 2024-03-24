package org.massmanagement.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.massmanagement.dto.RoleDTO;
import org.massmanagement.model.UserRole;
import org.massmanagement.repository.UserRoleRepo;
import org.springframework.stereotype.Service;

import javax.management.relation.Role;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserRoleService {
    private final UserRoleRepo userRoleRepo;
    public RoleDTO save(UserRole userRole) {
        log.info("Saving user role : {}", userRole);

        if(userRole == null || userRole.getRole().isEmpty()){
            return null;
        }

        userRole.setRole(userRole.getRole().toUpperCase());

        if(!userRole.getRole().startsWith("ROLE_")){
            userRole.setRole("ROLE_".concat(userRole.getRole()));
        }

        var saved = userRoleRepo.save(userRole);
        return convertToDTO(saved);
    }

    public RoleDTO getById(long id) {
        log.info("Getting user role by id : {}", id);
        var role = userRoleRepo.findById(id).orElse(new UserRole());
        return convertToDTO(role);
    }

    public RoleDTO getByRole(String role) {
        log.info("Getting user role by role name : {}", role);

        if(role.isEmpty()) return new RoleDTO();

        role = role.toUpperCase();

        if(!role.startsWith("ROLE_")){
            role = "ROLE_".concat(role);
        }

        var roleModel = userRoleRepo.findByRole(role).orElse(new UserRole());
        return convertToDTO(roleModel);
    }

    public List<RoleDTO> getAll() {
        log.info("Getting all user roles.");
        var all = userRoleRepo.findAll();
        return all.stream().map(this::convertToDTO).toList();
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

    public RoleDTO convertToDTO(UserRole userRole){
        if(userRole == null) return new RoleDTO();
        return new RoleDTO(userRole.getId(), userRole.getRole().replace("ROLE_",""),userRole.getDescription());
    }
}
