package org.massmanagement.controller;

import lombok.RequiredArgsConstructor;
import org.massmanagement.model.UserRole;
import org.massmanagement.service.UserRoleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/user-role/v1")
@RequiredArgsConstructor
public class UserRoleController {
    private final UserRoleService userRoleService;
    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody UserRole role) {
        var saved = userRoleService.save(role);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getById(@PathVariable long id) {
        var role = userRoleService.getById(id);
        return ResponseEntity.ok(role);
    }

    @GetMapping("/get-by-role/{role}")
    public ResponseEntity<?> getByName(@PathVariable String role) {
        var roleModel = userRoleService.getByRole(role);
        return ResponseEntity.ok(roleModel);
    }

    @GetMapping("/get-all")
    public ResponseEntity<?> getAll() {
        var roleList = userRoleService.getAll();
        return ResponseEntity.ok(roleList);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        boolean deleted = userRoleService.delete(id);
        return deleted ? ResponseEntity.ok(Collections.singletonMap("message", "User Role deleted successfully.")) :
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error", "Could not delete role."));
    }
}
