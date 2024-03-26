package org.massmanagement.model.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReportConverter implements AttributeConverter<Map<String, Object>,String> {
    private final ObjectMapper mapper;
    @Override
    public String convertToDatabaseColumn(Map<String, Object> report) {
        try{
            return mapper.writeValueAsString(report);
        }catch (Exception ex){
            log.error("Error occurred in ReportConverter cause : {}",ex.getMessage());
            return null;
        }
    }

    @Override
    public Map<String, Object> convertToEntityAttribute(String dbData) {
        try{
            return mapper.readValue(dbData, new TypeReference<>() {
            });
        }catch (Exception ex){
            log.error("Error occurred in ReportConverter cause : {}",ex.getMessage());
            return null;
        }
    }
}
