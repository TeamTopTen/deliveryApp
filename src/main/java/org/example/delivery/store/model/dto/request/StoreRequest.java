package org.example.delivery.store.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.sql.Time;

public record StoreRequest(

    @NotBlank
    String name,

    @Pattern(
        regexp = "^(01[0-9])(\\d{3,4})(\\d{4})$",
        message = "올바른 전화번호 양식이 필요합니다."
    )
    String storeNumber,

    @NotBlank
    String storeAddress,

    @Pattern(
        regexp = "^\\d{3}-\\d{2}-\\d{5}$",
        message = "올바른 사업자번호 양식을 입력해주세요."
    )
    String registrationNumber,

    @NotBlank
    Integer minOrderPrice,

    @NotNull
    Time openingTime,

    @NotNull
    Time closingTime
) {

}