package org.massmanagement.dto;

public record ChangePasswordDTO(
        String currentPassword,
        String newPassword,
        String confirmPassword
) {
}
