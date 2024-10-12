package com.project.cafelogproject.config.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
  INVALID_INPUT(HttpStatus.BAD_REQUEST, "잘못된 입력입니다"),
  UNAUTHORIZED_ACCESS(HttpStatus.UNAUTHORIZED, "권한이 없습니다"),
  DUPLICATE_EMAIL(HttpStatus.CONFLICT, "이미 등록된 이메일입니다"),
  LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "로그인에 실패했습니다"),
  USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다"),
  POST_NOT_FOUND(HttpStatus.NOT_FOUND, "해당하는 태그 및 카페이름을 가진 게시글이 없습니다"),
  COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "댓글을 찾을 수 없습니다"),
  INVALID_POST_ID(HttpStatus.BAD_REQUEST, "게시글 ID가 유효하지 않습니다"),
  UNAUTHORIZED_COMMENT_DELETE(HttpStatus.UNAUTHORIZED, "댓글 삭제 권한이 없습니다"),
  UNAUTHORIZED_POST_DELETE(HttpStatus.UNAUTHORIZED, "게시글 삭제 권한이 없습니다"),
  INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다"),
  UNAUTHORIZED_POST_UPDATE(HttpStatus.UNAUTHORIZED, "게시글 수정 권한이 없습니다"),
  UNAUTHORIZED_COMMENT_UPDATE(HttpStatus.UNAUTHORIZED, "댓글 수정 권한이 없습니다"),
  FAVORITE_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 즐겨찾기에 추가된 게시글입니다"),
  INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "유효하지 않은 이메일 또는 비밀번호입니다.");

  private final HttpStatus httpStatus;
  private final String detail;

  ErrorCode(HttpStatus httpStatus, String detail) {
    this.httpStatus = httpStatus;
    this.detail = detail;
  }
}