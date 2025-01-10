package org.example.delivery.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
  //Base
  INVALID_REQUEST(HttpStatus.BAD_REQUEST,  "유효하지 않은 요청입니다."),
  ACCESS_DENIED(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),
  NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 엔티티입니다."),
  METHOD_NOT_AllOWED(HttpStatus.METHOD_NOT_ALLOWED,"잘못된 HTTP 메서드를 호출했습니다."),
  CONFLICT(HttpStatus.CONFLICT, "이미 존재하는 엔티티입니다."),
  INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"서버에 에러가 발생했습니다."),

  //User
  AUTHENTICATION_FAILED(HttpStatus.UNAUTHORIZED, "이메일 혹은 비밀번호가 일치하지 않습니다."),
  USER_INVALID_ROLE(HttpStatus.BAD_REQUEST, "유효하지 않은 권한입니다. USER 또는 OWNER만 선택 가능합니다."),
  USER_ACCESS_DENIED(HttpStatus.FORBIDDEN,"접근 권한이 없습니다."),
  USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 유저입니다"),
  USER_CONFLICT(HttpStatus.CONFLICT, "이미 존재하는 유저입니다."),

  //Token
  TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED,"유효하지 않은 토큰입니다."),
  TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 토큰입니다."),

  //Menu
  Menu_BAD_REQUEST(HttpStatus.BAD_REQUEST, "메뉴 작성할 수 없습니다."),
  MENU_ACCESS_DENIED(HttpStatus.FORBIDDEN, "메뉴는 해당 작성자만 수정 삭제 할 수 있습니다."),
  MENU_NOT_FOUND(HttpStatus.NOT_FOUND, "일치하는 정보가 없습니다."),

  //Order
  ORDER_ACCESS_DENIED(HttpStatus.FORBIDDEN, "주문은 유저만 등록/삭제 할 수 있습니다."),
  ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 주문입니다."),
  ORDER_STATUS_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 주문입니다."),
  ORDER_MIN_PRICE_BAD_REQUEST(HttpStatus.BAD_REQUEST, "가게에서 설정한 최소 주문 금액을 만족해야 주문이 가능합니다."),
  ORDER_TIME_BAD_REQUEST(HttpStatus.BAD_REQUEST, "가게의 오픈/마감 시간이 지나면 주문할 수 없습니다."),

  //Review
  REVIEW_ALREADY_EXISTS(HttpStatus.CONFLICT, "리뷰가 이미 존재합니다."),
  REVIEW_NOT_ORDER_COMPLETED(HttpStatus.FORBIDDEN, "배달 완료 되지 않은 주문은 리뷰를 작성할 수 없습니다."),
  REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "리뷰를 찾을 수 없습니다."),

  //Store
  STORE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 매장입니다"),
  TOO_MANY_STORES(HttpStatus.BAD_REQUEST, "매장 등록은 최대 3개까지 가능합니다."),
  STORE_NAME_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 존재하는 매장 이름입니다"),
  STORE_ADDRESS_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 존재하는 매장 주소입니다")

  ;
  private final HttpStatus status;
  private final String message;

  ErrorCode(final HttpStatus status, final String message) {
    this.status = status;
    this.message = message;
  }
}
