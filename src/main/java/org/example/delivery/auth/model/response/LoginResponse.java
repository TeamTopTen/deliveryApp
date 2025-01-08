package org.example.delivery.auth.model.response;

public record LoginResponse(
    Long id,
    String accessToken,
    String refreshToken
) {
}
