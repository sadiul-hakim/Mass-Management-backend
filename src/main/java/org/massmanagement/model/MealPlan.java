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
public class MealPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String item;
    private long period;
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp date = new Timestamp(System.currentTimeMillis());
}
