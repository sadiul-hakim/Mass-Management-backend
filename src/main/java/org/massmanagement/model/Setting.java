package org.massmanagement.model;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.massmanagement.model.converter.MapConverter;
import org.massmanagement.model.converter.NumberListConverter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
public class Setting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true)
    private String name;

    @Convert(converter = MapConverter.class)
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> properties = new HashMap<>();

    @Convert(converter = NumberListConverter.class)
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private List<Long> excludeTransactionTypes = new ArrayList<>();

    @Convert(converter = NumberListConverter.class)
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private List<Long> billTypes = new ArrayList<>();

    public long getPropertyLong(String name) {

        if (name.isEmpty() || !properties.containsKey(name)) {
            return 0;
        }

        Object obj = properties.get(name);
        if (obj instanceof Number number) {
            return number.longValue();
        }

        return 0;
    }

    public String getPropertyString(String name) {

        if (name.isEmpty() || !properties.containsKey(name)) {
            return "";
        }

        Object obj = properties.get(name);
        if (obj instanceof String text) {
            return text;
        }

        return "";
    }

    public Setting(long id, String name, Map<String, Object> properties, List<Long> excludeTransactionTypes, List<Long> billTypes) {
        this.id = id;
        this.name = name;
        this.properties = properties;
        this.excludeTransactionTypes = excludeTransactionTypes;
        this.billTypes = billTypes;
    }

    public Setting() {
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

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    public List<Long> getExcludeTransactionTypes() {
        return excludeTransactionTypes;
    }

    public void setExcludeTransactionTypes(List<Long> excludeTransactionTypes) {
        this.excludeTransactionTypes = excludeTransactionTypes;
    }

    public List<Long> getBillTypes() {
        return billTypes;
    }

    public void setBillTypes(List<Long> billTypes) {
        this.billTypes = billTypes;
    }
}
