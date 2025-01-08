package org.example.delivery.auth.model.response;

import org.example.delivery.auth.model.UserRole;

public record RegisterResponse(
    Long id,
    String name,
    UserRole userRole
){
}
