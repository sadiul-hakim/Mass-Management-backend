package org.massmanagement.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.massmanagement.model.converter.MapConverter;
import org.massmanagement.model.converter.NumberListConverter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Setting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(unique = true)
    private String name;
    @Convert(converter = MapConverter.class)
    @Column(columnDefinition = "JSON")
    private Map<String,Long> properties = new HashMap<>();
    @Convert(converter = NumberListConverter.class)
    @Column(columnDefinition = "JSON")
    private List<Long> excludeTransactionTypes = new ArrayList<>();


    public long getProperty(String name){

        if(name.isEmpty() || !properties.containsKey(name)){
            return 0;
        }

        return properties.get(name);
    }
}
