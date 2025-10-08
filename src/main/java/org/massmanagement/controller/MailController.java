package org.massmanagement.controller;

import org.massmanagement.dto.MailStructure;
import org.massmanagement.service.MailService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequestMapping("/mail/v1")
class MailController {
    private final MailService mailService;

    MailController(MailService mailService) {
        this.mailService = mailService;
    }

    @PostMapping(value = "/send", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> send(@RequestBody MailStructure mail) {
        mailService.send(mail);
        return ResponseEntity.ok(Collections.singletonMap("message", "Mail sent successfully."));
    }
}
