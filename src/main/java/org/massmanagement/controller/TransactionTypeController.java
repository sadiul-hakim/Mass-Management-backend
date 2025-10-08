package org.massmanagement.controller;

import org.massmanagement.model.TransactionType;
import org.massmanagement.service.TransactionTypeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/transaction-type/v1")
class TransactionTypeController {
    private final TransactionTypeService transactionTypeService;

    TransactionTypeController(TransactionTypeService transactionTypeService) {
        this.transactionTypeService = transactionTypeService;
    }

    @PostMapping(value = "/add", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> add(@RequestBody TransactionType transactionType) {
        var savedType = transactionTypeService.save(transactionType);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedType);
    }

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TransactionType> getById(@PathVariable long id) {
        var transactionType = transactionTypeService.getById(id);
        return ResponseEntity.ok(transactionType);
    }

    @GetMapping(value = "/get-all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAll() {
        var transactionTypes = transactionTypeService.getAll();
        return ResponseEntity.ok(transactionTypes);
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> delete(@PathVariable long id) {
        boolean deleted = transactionTypeService.delete(id);
        return deleted ? ResponseEntity.ok(Collections.singletonMap("message", "Type deleted successfully.")) :
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error",
                        "Could not delete type. Make sure type is not in use."));
    }
}
