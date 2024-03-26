package org.massmanagement.controller;

import lombok.RequiredArgsConstructor;
import org.massmanagement.dto.MailStructure;
import org.massmanagement.service.MailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/mail/v1")
@RequiredArgsConstructor
public class MailController {
    private final MailService mailService;
    @PostMapping("/send")
    public ResponseEntity<?> send(@RequestParam String toMail, @RequestBody MailStructure mail){
        mailService.send(toMail,mail);
        return ResponseEntity.ok(Collections.singletonMap("message","Mail sent successfully."));
    }
}
