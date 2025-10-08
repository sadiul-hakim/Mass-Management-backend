package org.massmanagement.model;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
public class Rule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String rule;
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp date = new Timestamp(System.currentTimeMillis());

    public Rule(long id, String rule, Timestamp date) {
        this.id = id;
        this.rule = rule;
        this.date = date;
    }

    public Rule() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }
}
