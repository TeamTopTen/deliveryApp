package org.example.delivery.common.exception.base;

import org.example.delivery.common.exception.ErrorCode;

public class NotFoundException extends BusinessException {

  public NotFoundException(ErrorCode errorCode) {
    super(errorCode);
  }
}
