package org.example.delivery.common.exception.base;

import org.example.delivery.common.exception.ErrorCode;

public class AuthException extends BusinessException {

  public AuthException(ErrorCode errorCode) {
    super(errorCode);
  }
}
