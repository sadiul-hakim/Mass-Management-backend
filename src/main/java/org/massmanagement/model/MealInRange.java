package org.massmanagement.model;

import java.sql.Timestamp;

public class MealInRange {
    private long id;
    private long userId;
    private long type;
    private double amount;
    private Timestamp startDate = new Timestamp(System.currentTimeMillis());
    private Timestamp endDate = new Timestamp(System.currentTimeMillis());
    private long period;

    public MealInRange() {
    }

    public MealInRange(long id, long userId, long type, double amount, Timestamp startDate, Timestamp endDate,
                       long period) {
        this.id = id;
        this.userId = userId;
        this.type = type;
        this.amount = amount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.period = period;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getType() {
        return type;
    }

    public void setType(long type) {
        this.type = type;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    public Timestamp getEndDate() {
        return endDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }

    public long getPeriod() {
        return period;
    }

    public void setPeriod(long period) {
        this.period = period;
    }
}
