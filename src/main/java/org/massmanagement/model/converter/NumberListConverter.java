package org.massmanagement.model.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class NumberListConverter implements AttributeConverter<List<Long>,String> {
    private final ObjectMapper mapper;
    @Override
    public String convertToDatabaseColumn(List<Long> attribute) {

        try{
            return mapper.writeValueAsString(attribute);
        }catch (Exception ex){
            log.error("Error occurred. Cause {}",ex.getMessage());
            return null;
        }
    }

    @Override
    public List<Long> convertToEntityAttribute(String dbData) {
        try{
            return mapper.readValue(dbData, new TypeReference<>() {
            });
        }catch (Exception ex){
            log.error("Error occurred. Cause {}",ex.getMessage());
            return null;
        }
    }
}
