package org.massmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.massmanagement.model.UserStatus;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private long id;
    private String name;
    private String phone;
    private String email;
    private String address;
    private RoleDTO role;
    private UserStatus status;
    private String joiningDate;
}
