package org.massmanagement.dto;

import org.massmanagement.model.MealType;
import org.massmanagement.model.Period;

public record MealDTO(
        long id,
        UserDTO user,
        MealType type,
        int amount,
        String date,
        Period period
) {
}
