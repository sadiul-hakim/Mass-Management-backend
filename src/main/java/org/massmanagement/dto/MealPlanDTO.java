package org.massmanagement.dto;

import org.massmanagement.model.Period;

import java.sql.Timestamp;

public record MealPlanDTO(
        long id,
        String item,
        Period period,
        String date
) {
}
