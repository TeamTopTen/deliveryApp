package org.example.delivery.common.exception.base;

import org.example.delivery.common.exception.ErrorCode;

public class InvalidRequestException extends BusinessException {
    public InvalidRequestException(ErrorCode errorCode) {
        super(errorCode);
    }
}
