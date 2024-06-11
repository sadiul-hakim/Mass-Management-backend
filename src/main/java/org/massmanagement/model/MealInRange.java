package org.massmanagement.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MealInRange {
    private long id;
    private long userId;
    private long type;
    private double amount;
    private Timestamp startDate = new Timestamp(System.currentTimeMillis());
    private Timestamp endDate = new Timestamp(System.currentTimeMillis());
    private long period;
}
