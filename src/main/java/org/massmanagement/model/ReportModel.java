package org.massmanagement.model;

import jakarta.persistence.*;
import org.massmanagement.model.converter.ReportConverter;

import java.util.Map;

@Entity
public class ReportModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Convert(converter = ReportConverter.class)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> report;

    public ReportModel() {
    }

    public ReportModel(long id, Map<String, Object> report) {
        this.id = id;
        this.report = report;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Map<String, Object> getReport() {
        return report;
    }

    public void setReport(Map<String, Object> report) {
        this.report = report;
    }
}
