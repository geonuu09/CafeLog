package com.project.cafelogproject.config.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(CustomException.class)
  protected ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
    log.error("핸들링한 커스텀 예외: {}", e.getErrorCode());
    return ErrorResponse.toResponseEntity(e.getErrorCode());
  }

  @ExceptionHandler(Exception.class)
  protected ResponseEntity<ErrorResponse> handleException(Exception e) {
    log.error("핸들링하지 않은 예외: {}", e.getMessage());
    ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
    return ErrorResponse.toResponseEntity(errorCode);
  }
}