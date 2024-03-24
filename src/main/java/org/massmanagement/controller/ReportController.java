package org.massmanagement.controller;

import lombok.RequiredArgsConstructor;
import org.massmanagement.service.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/report/v1")
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;

    @GetMapping("/generate")
    public ResponseEntity<?> generate(){
        var report = reportService.generateReport();
        return ResponseEntity.ok(report);
    }
}
