package org.massmanagement.model;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
public class Cost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long type;
    private long amount;
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp date = new Timestamp(System.currentTimeMillis());

    public Cost() {
    }

    public Cost(long id, long type, long amount, Timestamp date) {
        this.id = id;
        this.type = type;
        this.amount = amount;
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getType() {
        return type;
    }

    public void setType(long type) {
        this.type = type;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }
}
