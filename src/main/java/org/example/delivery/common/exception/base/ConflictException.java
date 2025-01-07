package org.example.delivery.common.exception.base;

import org.example.delivery.common.exception.ErrorCode;

public class ConflictException extends BusinessException {

  public ConflictException(ErrorCode errorCode) {
    super(errorCode);
  }
}
