package org.example.delivery.auth.model.response;

import org.example.delivery.common.domain.enums.UserRole;

public record RegisterResponse(

    Long id,
    String name,
    UserRole userRole
){
}
