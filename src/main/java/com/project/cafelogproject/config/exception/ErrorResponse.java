package com.project.cafelogproject.config.exception;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

@Getter
@Builder
public class ErrorResponse {
  private final LocalDateTime timestamp = LocalDateTime.now();
  private final String error;
  private final String code;
  private final String message;

  public static ResponseEntity<ErrorResponse> toResponseEntity(ErrorCode errorCode) {
    return ResponseEntity
        .status(errorCode.getHttpStatus())
        .body(ErrorResponse.builder()
            .error(errorCode.getHttpStatus().name())
            .code(errorCode.name())
            .message(errorCode.getDetail())
            .build()
        );
  }
}