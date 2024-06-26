package org.massmanagement.controller;

import lombok.RequiredArgsConstructor;
import org.massmanagement.model.Rule;
import org.massmanagement.service.RuleService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/rule/v1")
@RequiredArgsConstructor
class RuleController {
    private final RuleService ruleService;

    @GetMapping(value = "/get-all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAll() {
        List<Rule> ruleList = ruleService.getAll();
        return ResponseEntity.ok(ruleList);
    }

    @PostMapping(value = "/add", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> add(@RequestBody Rule rule) {
        Rule save = ruleService.save(rule);
        return ResponseEntity.ok(save);
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> delete(@PathVariable long id) {
        boolean deleted = ruleService.delete(id);
        return ResponseEntity.ok(Collections.singletonMap("message", "Rule deleted successfully!"));
    }
}
