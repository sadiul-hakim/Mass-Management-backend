package org.massmanagement.service;

import com.lowagie.text.Font;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.massmanagement.model.ReportModel;
import org.massmanagement.repository.ReportRepo;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportModelService {
    private final ReportRepo reportRepo;
    private final IncomeService incomeService;
    private final MealService mealService;
    private final CostService costService;
    private final ReportService reportService;

    public boolean saveAndCleanUp(ReportModel reportModel) {
        log.info("Saving report and cleaning database.");
        try {

            log.info("Saving report.");
            ReportModel save = reportRepo.save(reportModel);
            if (save.getId() == 0) return false;

            log.info("Cleaning income.");
            incomeService.deleteAll();

            log.info("Cleaning costs.");
            costService.deleteAll();

            log.info("Cleaning meals.");
            mealService.deleteAll();

            return true;
        } catch (Exception ex) {
            log.error("Could not save and clean. Cause {}", ex.getMessage());
            return false;
        }
    }

    public void createPdf(HttpServletResponse httpResponse) {
        log.info("Creating pdf.");

        String titleText = "Monthly Report";

        var report = reportService.generateReport();

        try (Document pdf = new Document(PageSize.A4)) {
            PdfWriter.getInstance(pdf, httpResponse.getOutputStream());
            pdf.open();

            // PDF Title
            Font titleFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 25, Color.blue);
            Paragraph title = new Paragraph(titleText, titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            pdf.add(title);

            writeReport(pdf, report);

        } catch (Exception ex) {
            log.error("Error occurred while creating document. Cause {}", ex.getMessage());
        }
    }

    private void writeReport(Document pdf, Map<String, Object> report) {
        Font font10 = FontFactory.getFont(FontFactory.HELVETICA, 10);
        Font font14 = FontFactory.getFont(FontFactory.HELVETICA, 14);
        Font subTitleFont = FontFactory.getFont(FontFactory.HELVETICA, 16, Color.black);

        var subTitle = new Paragraph("Overview", subTitleFont);
        subTitle.setSpacingAfter(10);
        pdf.add(subTitle);

        // Table
        float width = pdf.getPageSize().getWidth();

        float[] columnDefinitionSize = {14.29F, 14.29F, 14.29F, 14.29F, 14.29F, 14.29F, 14.29F};

        PdfPTable table = getTable(columnDefinitionSize, width);

        addCell("Date", table, font14);
        addCell("Total Income", table, font14);
        addCell("Total Cost", table, font14);
        addCell("Total Borders", table, font14);
        addCell("Meal Rate", table, font14);
        addCell("Other Cost", table, font14);
        addCell("Total Meals", table, font14);


        addCell(String.valueOf(report.get("date")), table, font10);
        addCell(String.valueOf(report.get("total_income")), table, font10);
        addCell(String.valueOf(report.get("total_cost")), table, font10);
        addCell(String.valueOf(report.get("total_borders")), table, font10);
        addCell(String.valueOf(report.get("mealRate")), table, font10);
        addCell(String.valueOf(report.get("other_cost")), table, font10);
        addCell(String.valueOf(report.get("total_meals")), table, font10);

        pdf.add(table);

        writeBorderInfo(pdf, report, width, font10, font14, columnDefinitionSize, subTitleFont);
    }

    private void writeBorderInfo(Document pdf, Map<String, Object> report, float width,
                                 Font font10, Font font14, float[] columnDefinitionSize, Font subTitleFont) {

        var subTitle = new Paragraph("Borders", subTitleFont);
        subTitle.setSpacingAfter(10);
        pdf.add(subTitle);

        PdfPTable table = getTable(columnDefinitionSize, width);

        addCell("Border", table, font14);
        addCell("Meals", table, font14);
        addCell("Meal Cost", table, font14);
        addCell("Other Cost", table, font14);
        addCell("Total Cost", table, font14);
        addCell("Deposit", table, font14);
        addCell("Balance", table, font14);

        var borders = (List<Map<String, Object>>) report.get("borders");
        for (var border : borders) {
            var singleBorder = (Map<String, Object>) border.get("border");

            addCell(String.valueOf(singleBorder.get("name")), table, font10);
            addCell(String.valueOf(border.get("meals")), table, font10);
            addCell(String.valueOf(border.get("meal_cost")), table, font10);
            addCell(String.valueOf(border.get("other_cost")), table, font10);
            addCell(String.valueOf(border.get("total_cost")), table, font10);
            addCell(String.valueOf(border.get("deposit")), table, font10);
            addCell(String.valueOf(border.get("balance")), table, font10);
        }

        pdf.add(table);
    }

    private PdfPTable getTable(float[] columnDefinitionSize, float width) {

        PdfPTable table = new PdfPTable(columnDefinitionSize);
        table.getDefaultCell().setBorder(1);
        table.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.setTotalWidth(width - 72);
        table.setLockedWidth(true);

        return table;
    }

    private void addCell(String value, PdfPTable table, Font font) {

        PdfPCell cell = new PdfPCell(new Phrase(value, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
    }
}
