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
public class MapConverter implements AttributeConverter<Map<String, Object>, String> {
    private final ObjectMapper mapper;

    private static final Logger log = LoggerFactory.getLogger(MapConverter.class);

    public MapConverter(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public String convertToDatabaseColumn(Map<String, Object> attribute) {
        try {
            return mapper.writeValueAsString(attribute);
        } catch (Exception ex) {
            log.error("Error occurred. Cause {}", ex.getMessage());
            throw new IllegalArgumentException("Error converting map to JSON", ex);
        }
    }

    @Override
    public Map<String, Object> convertToEntityAttribute(String dbData) {
        try {
            return mapper.readValue(dbData, new TypeReference<>() {
            });
        } catch (Exception ex) {
            log.error("Error occurred. Cause {}", ex.getMessage());
            return Collections.emptyMap();
        }
    }
}
