package org.massmanagement.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MealInRange {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long userId;
    private long type;
    private int amount;
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp startDate = new Timestamp(System.currentTimeMillis());
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp endDate = new Timestamp(System.currentTimeMillis());
}
