package org.example.delivery.store.model.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.sql.Time;

public record StoreRequest(

    @NotBlank(message = "이름은 필수 입력값입니다.")
    String name,

    @Pattern(
        regexp = "^(01[0-9])(\\d{3,4})(\\d{4})$",
        message = "휴대폰 번호는 숫자만 입력할 수 있습니다."
    )
    String storeNumber,

    @NotBlank(message = "주소는 필수 입력값입니다.")
    String storeAddress,

    @Pattern(
        regexp = "^\\d{3}-\\d{2}-\\d{5}$",
        message = "사업자 번호는 필수 입력값입니다. 000-00-00000와 같은 양식으로 입력해주세요."
    )
    String registrationNumber,

    @NotNull(message = "최소 주문금액은 필수 입력값입니다.")
    Integer minOrderPrice,

    @JsonFormat(pattern = "HH:mm:ss")
    @NotNull(message = "가게 여는 시간은 필수 입력값입니다. hh:mm:ss 양식으로 입력해주세요")
    Time openingTime,

    @JsonFormat(pattern = "HH:mm:ss")
    @NotNull(message = "가게 여는 시간은 필수 입력값입니다. hh:mm:ss 양식으로 입력해주세요")
    Time closingTime
) {

}