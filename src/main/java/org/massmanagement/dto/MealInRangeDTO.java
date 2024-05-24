package org.massmanagement.dto;

import org.massmanagement.model.MealType;

public record MealInRangeDTO(
        long id,
        UserDTO user,
        MealType type,
        int amount,
        String startDate,
        String endDate
) {
}
