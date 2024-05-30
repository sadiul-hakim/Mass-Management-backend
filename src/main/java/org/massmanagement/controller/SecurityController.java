package org.massmanagement.controller;

import lombok.RequiredArgsConstructor;
import org.massmanagement.dto.Token;
import org.massmanagement.service.SecurityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequestMapping("/security/v1")
@RequiredArgsConstructor
public class SecurityController {
    private final SecurityService securityService;

    @PostMapping("/validate-token")
    public ResponseEntity<?> validateToken(@RequestBody Token token) {
        boolean valid = securityService.validateToken(token);
        return valid ? ResponseEntity.ok(Collections.singletonMap("message","Valid token!")) :
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error","Invalid token"));
    }
}
