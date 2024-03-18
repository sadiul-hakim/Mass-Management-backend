package org.massmanagement.controller;

import lombok.RequiredArgsConstructor;
import org.massmanagement.model.User;
import org.massmanagement.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/user/v1")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody User user) {
        var saved = userService.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getById(@PathVariable long id) {
        var user = userService.getById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/get-by-role/{roleId}")
    public ResponseEntity<?> getByRole(@PathVariable long roleId) {
        var user = userService.getByRole(roleId);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/get-all")
    public ResponseEntity<?> getAll() {
        var userList = userService.getAll();
        return ResponseEntity.ok(userList);
    }

    @GetMapping("/change-manager")
    public ResponseEntity<?> assignRole(@RequestParam long managerId,@RequestParam long userId){
        boolean assigned = userService.changeManager(managerId,userId);
        return assigned ? ResponseEntity.ok(Collections.singletonMap("message", STR."User \{userId} is assigned to role manager successfully.")) :
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error", "Could not assign role manager."));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        boolean deleted = userService.delete(id);
        return deleted ? ResponseEntity.ok(Collections.singletonMap("message", "User deleted successfully.")) :
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error", "Could not delete user."));
    }
}
