package org.massmanagement.controller;

import lombok.RequiredArgsConstructor;
import org.massmanagement.model.Rule;
import org.massmanagement.service.RuleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/rule/v1")
@RequiredArgsConstructor
public class RuleController {
    private final RuleService ruleService;

    @GetMapping("/get-all")
    public ResponseEntity<?> getAll(){
        List<Rule> ruleList = ruleService.getAll();
        return ResponseEntity.ok(ruleList);
    }

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody Rule rule){
        Rule save = ruleService.save(rule);
        return ResponseEntity.ok(save);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable long id){
        boolean deleted = ruleService.delete(id);
        return ResponseEntity.ok(Collections.singletonMap("message","Rule deleted successfully!"));
    }
}
