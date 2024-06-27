package org.massmanagement.dto;

public record Token(String token) {
    public Token() {
        this("");
    }
}
