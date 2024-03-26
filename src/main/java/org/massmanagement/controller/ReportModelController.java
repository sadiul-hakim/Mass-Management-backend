package org.massmanagement.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.massmanagement.model.ReportModel;
import org.massmanagement.service.ReportModelService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequiredArgsConstructor
@RequestMapping("/monthly-report/v1")
public class ReportModelController {
    private final ReportModelService reportModelService;

    @PostMapping("/add")
    public ResponseEntity<?> save(@RequestBody ReportModel reportModel) {
        boolean saved = reportModelService.saveAndCleanUp(reportModel);
        return saved ? ResponseEntity.ok(Collections.singletonMap("message", "Successfully saved report and cleaned up database")) :
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error", "Could not save or clean up database."));
    }

    @GetMapping("/export-pdf")
    public void exportInPdfForm(HttpServletResponse httpResponse){

        httpResponse.setContentType("application/pdf");

        reportModelService.createPdf(httpResponse);

        httpResponse.setHeader("Content-Disposition","attachment;filename=report.pdf");
    }
}
