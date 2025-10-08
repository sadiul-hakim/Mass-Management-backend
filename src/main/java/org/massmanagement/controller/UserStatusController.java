package org.massmanagement.controller;

import org.massmanagement.model.UserStatus;
import org.massmanagement.service.UserStatusService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/user-status/v1")
class UserStatusController {
    private final UserStatusService userStatusService;

    UserStatusController(UserStatusService userStatusService) {
        this.userStatusService = userStatusService;
    }

    @PostMapping(value = "/add", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> add(@RequestBody UserStatus status) {
        var saved = userStatusService.save(status);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getById(@PathVariable long id) {
        var status = userStatusService.getById(id);
        return ResponseEntity.ok(status);
    }

    @GetMapping(value = "/get/{status}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getByStatus(@PathVariable String status) {
        var statusModel = userStatusService.getByStatus(status);
        return ResponseEntity.ok(statusModel);
    }

    @GetMapping(value = "/get-all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAll() {
        var statusList = userStatusService.getAll();
        return ResponseEntity.ok(statusList);
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> delete(@PathVariable long id) {
        boolean deleted = userStatusService.delete(id);
        return deleted ? ResponseEntity.ok(Collections.singletonMap("message", "User Status deleted successfully.")) :
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error",
                        "Could not delete status. Make sure status is not in use."));
    }
}
