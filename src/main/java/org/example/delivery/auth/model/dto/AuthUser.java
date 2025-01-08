package org.example.delivery.auth.model.dto;

import lombok.Getter;
import org.example.delivery.auth.model.UserRole;

@Getter
public record AuthUser(
    Long id,
    String email,
    UserRole userRole
) {
}
