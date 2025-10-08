package org.massmanagement.model.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;

@Component
@Converter
public class ReportConverter implements AttributeConverter<Map<String, Object>, String> {
    private final ObjectMapper mapper;

    private static final Logger log = LoggerFactory.getLogger(ReportConverter.class);

    public ReportConverter(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public String convertToDatabaseColumn(Map<String, Object> report) {
        try {
            return mapper.writeValueAsString(report);
        } catch (Exception ex) {
            log.error("Error occurred in ReportConverter cause : {}", ex.getMessage());
            throw new IllegalArgumentException("Error converting report to JSON", ex);
        }
    }

    @Override
    public Map<String, Object> convertToEntityAttribute(String dbData) {
        try {
            return mapper.readValue(dbData, new TypeReference<>() {
            });
        } catch (Exception ex) {
            log.error("Error occurred in ReportConverter cause : {}", ex.getMessage());
            return Collections.emptyMap();
        }
    }
}
