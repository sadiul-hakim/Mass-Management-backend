package org.massmanagement.dto;

import org.massmanagement.model.MealType;
import org.massmanagement.model.Period;

public record MealDTO(
        long id,
        UserDTO user,
        MealType type,
        double amount,
        String date,
        Period period
) {

    public MealDTO() {
        this(0, null, null, 0, "", null);
    }
}
