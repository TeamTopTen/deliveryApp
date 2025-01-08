package org.example.delivery.auth.model.dto;

import org.example.delivery.auth.model.UserRole;

public record AuthUser(
    Long id,
    String email,
    UserRole userRole
) {
}
