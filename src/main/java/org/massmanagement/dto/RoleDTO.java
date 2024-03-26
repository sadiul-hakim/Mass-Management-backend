package org.massmanagement.dto;

public record RoleDTO(long id, String role, String description) {
    public RoleDTO(){
        this(0,"","");
    }
}
