package org.example.delivery.auth.model.dto;

import org.example.delivery.auth.model.UserRole;
// 인증 객체
public record AuthUser(

    Long id,
    String email,
    UserRole userRole
) {
}
