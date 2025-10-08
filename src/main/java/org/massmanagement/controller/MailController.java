package org.massmanagement.controller;

import lombok.RequiredArgsConstructor;
import org.massmanagement.dto.MailStructure;
import org.massmanagement.service.MailService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/mail/v1")
@RequiredArgsConstructor
class MailController {
    private final MailService mailService;

    @PostMapping(value = "/send", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> send(@RequestBody MailStructure mail) {
        mailService.send(mail);
        return ResponseEntity.ok(Collections.singletonMap("message", "Mail sent successfully."));
    }
}
