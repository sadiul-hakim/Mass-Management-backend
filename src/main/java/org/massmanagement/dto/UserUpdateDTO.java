package org.massmanagement.dto;

public record UserUpdateDTO(
        long id, String name, String phone, String email,
        String address, long status
) {

    public UserUpdateDTO() {
        this(0, "", "", "", "", 0);
    }
}
