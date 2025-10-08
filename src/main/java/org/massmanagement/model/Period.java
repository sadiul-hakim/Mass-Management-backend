package org.massmanagement.model;

import jakarta.persistence.*;

@Entity
public class Period {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(unique = true)
    private String name;
    private String description;
    @Column(unique = true)
    private int periodOrder;
    private double meal;

    public Period() {
    }

    public Period(long id, String name, String description, int periodOrder, double meal) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.periodOrder = periodOrder;
        this.meal = meal;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPeriodOrder() {
        return periodOrder;
    }

    public void setPeriodOrder(int periodOrder) {
        this.periodOrder = periodOrder;
    }

    public double getMeal() {
        return meal;
    }

    public void setMeal(double meal) {
        this.meal = meal;
    }
}
