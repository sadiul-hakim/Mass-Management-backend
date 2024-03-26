package org.massmanagement.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.massmanagement.model.converter.ReportConverter;

import java.sql.Timestamp;
import java.util.Map;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Convert(converter = ReportConverter.class)
    @Column(columnDefinition = "JSON")
    private Map<String, Object> report;

    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp date;
}
