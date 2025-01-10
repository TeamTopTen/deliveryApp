package org.example.delivery.common.exception;

import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.example.delivery.common.exception.base.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getFieldErrors()
        .forEach(error -> errors.put(error.getField(), error.getDefaultMessage())
    );
    return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
  }

  //지원하지 않은 HTTP 메서드를 호출 할 경우 발생 (컨트롤러에서 정의되지 않은 Http 메서드 일 때)
  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  protected ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSuppoertedException(
      HttpRequestMethodNotSupportedException e) {
    log.error("HttpRequestMethodNotSupportedException", e);
    return createErrorResponseEntity(ErrorCode.METHOD_NOT_AllOWED);
  }

  @ExceptionHandler(BusinessException.class)
  protected ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
    log.error("BusinessException", e);
    return createErrorResponseEntity(e.getErrorCode());
  }

  @ExceptionHandler(Exception.class)
  protected ResponseEntity<ErrorResponse> handle(Exception e) {
    e.printStackTrace();
    log.error("Exception", e);
    return createErrorResponseEntity(ErrorCode.INTERNAL_SERVER_ERROR);
  }

  private ResponseEntity<ErrorResponse> createErrorResponseEntity(ErrorCode errorCode) {
    return new ResponseEntity<>(
        ErrorResponse.of(errorCode),
        errorCode.getStatus()
    );
  }
}
