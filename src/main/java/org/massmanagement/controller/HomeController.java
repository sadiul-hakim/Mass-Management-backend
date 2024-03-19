package org.massmanagement.controller;

import lombok.RequiredArgsConstructor;
import org.massmanagement.service.HomeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/home/v1")
@RequiredArgsConstructor
public class HomeController {
    private final HomeService homeService;
    @GetMapping("/totals")
    public ResponseEntity<?> getTotals(){
        Map<String, Long> totals = homeService.getTotals();
        return ResponseEntity.ok(totals);
    }

    @GetMapping("/border-info")
    public ResponseEntity<?> getBorderInfo(){
        List<Map<String, Object>> info = homeService.borderInformation();
        return ResponseEntity.ok(info);
    }
}
