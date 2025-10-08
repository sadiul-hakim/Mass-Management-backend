package org.massmanagement.model.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
@Converter
public class NumberListConverter implements AttributeConverter<List<Long>,String> {
    private final ObjectMapper mapper;

    private static final Logger log = LoggerFactory.getLogger(NumberListConverter.class);

    public NumberListConverter(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public String convertToDatabaseColumn(List<Long> attribute) {

        try{
            return mapper.writeValueAsString(attribute);
        }catch (Exception ex){
            log.error("Error occurred. Cause {}",ex.getMessage());
            throw new IllegalArgumentException("Error converting list to JSON", ex);
        }
    }

    @Override
    public List<Long> convertToEntityAttribute(String dbData) {
        try{
            return mapper.readValue(dbData, new TypeReference<>() {
            });
        }catch (Exception ex){
            log.error("Error occurred. Cause {}",ex.getMessage());
            return Collections.emptyList();
        }
    }
}
