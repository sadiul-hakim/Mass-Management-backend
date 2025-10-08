package org.massmanagement.model;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
public class MealPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String item;
    private long period;
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp date = new Timestamp(System.currentTimeMillis());

    public MealPlan() {
    }

    public MealPlan(long id, String item, long period, Timestamp date) {
        this.id = id;
        this.item = item;
        this.period = period;
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public long getPeriod() {
        return period;
    }

    public void setPeriod(long period) {
        this.period = period;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }
}
