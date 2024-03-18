package org.massmanagement.controller;

import lombok.RequiredArgsConstructor;
import org.massmanagement.model.UserStatus;
import org.massmanagement.service.UserStatusService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/user-status/v1")
@RequiredArgsConstructor
public class UserStatusController {
    private final UserStatusService userStatusService;
    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody UserStatus status) {
        var saved = userStatusService.save(status);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getById(@PathVariable long id) {
        var status = userStatusService.getById(id);
        return ResponseEntity.ok(status);
    }

    @GetMapping("/get-all")
    public ResponseEntity<?> getAll() {
        var statusList = userStatusService.getAll();
        return ResponseEntity.ok(statusList);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        boolean deleted = userStatusService.delete(id);
        return deleted ? ResponseEntity.ok(Collections.singletonMap("message", "User Status deleted successfully.")) :
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error", "Could not delete status."));
    }
}
