package org.example.delivery.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 커스텀 코드 규칙
 * - 맨 앞 글자는 각 도메인의 앞글자를 대문자로 바꾸어 입력해줍니다.
 * - 대문자 뒤 숫자코드는 어느 카테고리의 예외인지 입력해줍니다.
 * - 예외 작성 순서는 예외 카테고리의 숫자를 오름차순으로 입력해줍니다.
 * 1. status: 예외 카테고리 코드 2. code: 커스텀 코드(상태코드) 3.message: 예외 메시지
 */
@Getter
public enum ErrorCode {
  //Base
  INVALID_REQUEST(HttpStatus.BAD_REQUEST, "B400", "유효하지 않은 요청입니다."),
  ACCESS_DENIED(HttpStatus.FORBIDDEN, "B403", "접근 권한이 없습니다."),
  NOT_FOUND(HttpStatus.NOT_FOUND, "B404", "존재하지 않는 엔티티입니다."),
  METHOD_NOT_AllOWED(HttpStatus.METHOD_NOT_ALLOWED,"B405","잘못된 HTTP 메서드를 호풀했습니다."),
  CONFLICT(HttpStatus.CONFLICT, "B409", "이미 존재하는 엔티티입니다."),
  INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"B500","서버에 에러가 발생했습니다.")
  // VALIDATE(HttpStatus.BAD_REQUEST, "B400", "무슨 에러야?????"),invalid err 랑 묶을 수 있나요?

  //Menu

  //Order

  //Review

  //Store

  //User

  ;


  private final HttpStatus status;
  private final String code;
  private final String message;

  ErrorCode(final HttpStatus status, String code, final String message) {
    this.status = status;
    this.code = code;
    this.message = message;
  }
}
