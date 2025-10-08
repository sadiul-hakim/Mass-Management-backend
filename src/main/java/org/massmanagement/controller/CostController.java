package org.massmanagement.controller;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.massmanagement.model.Cost;
import org.massmanagement.service.CostService;
import org.massmanagement.util.RateLimiterConstant;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@Slf4j
@RestController
@RequestMapping("/cost/v1")
@RequiredArgsConstructor
class CostController {
    private final CostService costService;

    @PostMapping(value = "/add", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @RateLimiter(name = RateLimiterConstant.POST_REQUEST_RATE_LIMITER, fallbackMethod = "fallBack")
    public ResponseEntity<?> add(@RequestBody Cost cost) {
        var saved = costService.save(cost);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @RateLimiter(name = RateLimiterConstant.DEFAULT_RATE_LIMITER, fallbackMethod = "fallBack")
    public ResponseEntity<?> getById(@PathVariable long id) {
        var cost = costService.getById(id);
        return ResponseEntity.ok(cost);
    }

    @GetMapping(value = "/get-all", produces = MediaType.APPLICATION_JSON_VALUE)
    @RateLimiter(name = RateLimiterConstant.DEFAULT_RATE_LIMITER, fallbackMethod = "fallBack")
    public ResponseEntity<?> getAll() {
        var costList = costService.getAll();
        return ResponseEntity.ok(costList);
    }

    @GetMapping(value = "/get-all-by-type/{type}", produces = MediaType.APPLICATION_JSON_VALUE)
    @RateLimiter(name = RateLimiterConstant.DEFAULT_RATE_LIMITER, fallbackMethod = "fallBack")
    public ResponseEntity<?> getAllByType(@PathVariable long type) {
        var costList = costService.getAllByType(type);
        return ResponseEntity.ok(costList);
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @RateLimiter(name = RateLimiterConstant.DEFAULT_RATE_LIMITER, fallbackMethod = "fallBack")
    public ResponseEntity<?> delete(@PathVariable long id) {
        boolean deleted = costService.delete(id);
        return deleted ? ResponseEntity.ok(Collections.singletonMap("message", "Cost deleted successfully.")) :
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error", "Could not delete cost."));
    }

    private ResponseEntity<?> fallBack(Exception ex) {
        log.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(Collections.singletonMap("message", "Too many Calls!"));
    }
}
