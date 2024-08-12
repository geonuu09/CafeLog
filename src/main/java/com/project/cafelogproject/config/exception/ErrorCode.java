package com.project.cafelogproject.config.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
@AllArgsConstructor

public enum ErrorCode {
  INVALID_INPUT(BAD_REQUEST, "잘못된 입력입니다"),

  UNAUTHORIZED_ACCESS(UNAUTHORIZED, "권한이 없습니다"),
  DUPLICATE_EMAIL(BAD_REQUEST, "이미 등록된 이메일입니다"),
  LOGIN_FAILED(UNAUTHORIZED, "로그인에 실패했습니다"),
  USER_NOT_FOUND(NOT_FOUND, "사용자를 찾을 수 없습니다"),

  INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다");

  private final HttpStatus httpStatus;
  private final String detail;
}

