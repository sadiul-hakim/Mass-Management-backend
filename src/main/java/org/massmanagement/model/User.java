package org.massmanagement.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.massmanagement.model.deserializer.UserDeserializer;

import java.sql.Timestamp;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonDeserialize(using = UserDeserializer.class)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    @Column(unique = true)
    private String phone;
    @Column(unique = true)
    private String email;
    private String password;
    private String address;
    @ManyToOne
    private UserRole role;
    private long status;
    private Timestamp joiningDate = new Timestamp(System.currentTimeMillis());
}
