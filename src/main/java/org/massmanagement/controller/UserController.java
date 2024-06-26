package org.massmanagement.controller;

import lombok.RequiredArgsConstructor;
import org.massmanagement.dto.ChangePasswordDTO;
import org.massmanagement.dto.UserUpdateDTO;
import org.massmanagement.model.User;
import org.massmanagement.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/user/v1")
@RequiredArgsConstructor
class UserController {
    private final UserService userService;

    @PostMapping(value = "/add", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> add(@RequestBody User user) {
        var saved = userService.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PostMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(@RequestBody UserUpdateDTO user) {
        var saved = userService.update(user);

        return saved ? ResponseEntity.ok(Collections.singletonMap("message", "User is updated successfully!")) :
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("message", "Could not update user!"));
    }

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getById(@PathVariable long id) {
        var user = userService.getById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping(value = "/get-by-role/{roleId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getByRole(@PathVariable long roleId) {
        var user = userService.getByRole(roleId);
        return ResponseEntity.ok(user);
    }

    @GetMapping(value = "/get-all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAll() {
        var userList = userService.getAll();
        return ResponseEntity.ok(userList);
    }

    @GetMapping(value = "/change-manager", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> assignRole(@RequestParam long managerId, @RequestParam long userId) {
        boolean assigned = userService.changeManager(managerId, userId);
        return assigned ? ResponseEntity.ok(Collections.singletonMap("message", String.format("User %s is assigned to role manager successfully.", userId))) :
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error", "Could not assign role manager."));
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> delete(@PathVariable long id) {
        boolean deleted = userService.delete(id);
        return deleted ? ResponseEntity.ok(Collections.singletonMap("message", "User deleted successfully.")) :
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error", "Could not delete user."));
    }

    @PostMapping(value = "/change-password", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordDTO dto) {
        boolean changed = userService.changePassword(dto);

        return changed ? ResponseEntity.ok(Collections.singletonMap("message", "Password changed successfully!")) :
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("message", "Could not change password!"));
    }
}
