package org.example.delivery.auth.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record DeregisterRequest(

    @NotBlank
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,14}$",
        message = "비밀번호는 대소문자 포함 영문, 숫자, 특수 문자 최소 1글자를 포함하며 최소 8글자 이상으로 이루어져야 합니다."
    )
    String password
) {
}
