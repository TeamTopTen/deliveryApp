package org.example.delivery.user.model.response;

import org.example.delivery.user.model.UserRole;

public record SignUpResponse (
    Long userId,
    String name,
    UserRole userRole
){
}
