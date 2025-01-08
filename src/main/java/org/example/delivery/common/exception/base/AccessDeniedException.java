package org.example.delivery.common.exception.base;

import org.example.delivery.common.exception.ErrorCode;

public class AccessDeniedException extends BusinessException {

  public AccessDeniedException(ErrorCode errorCode) {
    super(errorCode);
  }
}
