package org.massmanagement.dto;

import org.massmanagement.model.UserStatus;


public record UserDTO(long id,String name,String phone,String email,
                      String address,RoleDTO role,UserStatus status,String joiningDate) {
}
