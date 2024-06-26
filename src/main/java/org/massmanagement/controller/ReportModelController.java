package org.massmanagement.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.massmanagement.model.ReportModel;
import org.massmanagement.service.ReportModelService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequiredArgsConstructor
@RequestMapping("/monthly-report/v1")
class ReportModelController {
    private final ReportModelService reportModelService;

    @PostMapping(value = "/add",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> save(@RequestBody ReportModel reportModel) {
        boolean saved = reportModelService.saveAndCleanUp(reportModel);
        return saved ? ResponseEntity.ok(Collections.singletonMap("message", "Successfully saved report and cleaned up database")) :
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error", "Could not save or clean up database."));
    }

    @DeleteMapping(value = "/delete/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> delete(@PathVariable long id){
        var deleted = reportModelService.delete(id);
        return deleted ? ResponseEntity.ok(Collections.singletonMap("message", "Successfully deleted report.")) :
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error", "Could not delete report."));
    }

    @GetMapping(value = "/get-all",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAll(){
        var all = reportModelService.getAll();
        return ResponseEntity.ok(all);
    }

    @GetMapping(value = "/export-pdf")
    public void exportInPdfForm(HttpServletResponse httpResponse){

        httpResponse.setContentType("application/pdf");

        reportModelService.createPdf(httpResponse);

        httpResponse.setHeader("Content-Disposition","attachment;filename=report.pdf");
    }
}
