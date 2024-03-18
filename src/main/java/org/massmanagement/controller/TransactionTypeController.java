package org.massmanagement.controller;

import lombok.RequiredArgsConstructor;
import org.massmanagement.model.TransactionType;
import org.massmanagement.service.TransactionTypeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/transaction-type/v1")
@RequiredArgsConstructor
public class TransactionTypeController {
    private final TransactionTypeService transactionTypeService;

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody TransactionType transactionType) {
        var savedType = transactionTypeService.save(transactionType);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedType);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<TransactionType> getById(@PathVariable long id) {
        var transactionType = transactionTypeService.getById(id);
        return ResponseEntity.ok(transactionType);
    }

    @GetMapping("/get-all")
    public ResponseEntity<?> getAll() {
        var transactionTypes = transactionTypeService.getAll();
        return ResponseEntity.ok(transactionTypes);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        boolean deleted = transactionTypeService.delete(id);
        return deleted ? ResponseEntity.ok(Collections.singletonMap("message", "Type deleted successfully.")) :
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error", "Could not delete type."));
    }
}
