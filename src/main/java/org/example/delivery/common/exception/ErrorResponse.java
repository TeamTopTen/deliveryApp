package org.example.delivery.common.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse {

  private HttpStatus errorCode;
  private String code;
  private String message;

  private ErrorResponse(ErrorCode errorCode) { //정적 팩토리 메서드에서 쓰려고 만든 생성자
    this.errorCode = errorCode.getStatus();
    this.code = errorCode.getCode();
    this.message = errorCode.getMessage();
  }

  public static ErrorResponse of(final ErrorCode errorCode) {
    return new ErrorResponse(errorCode);
  }
}
