package com.project.cafelogproject.controller;

import com.project.cafelogproject.dto.CommentDTO;
import com.project.cafelogproject.dto.UpdateCommentDTO;
import com.project.cafelogproject.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/posts/{postId}/comments")
@RequiredArgsConstructor
public class CommentController {

  private final CommentService commentService;

  /**
   * 댓글 추가 API
   *
   * @param postId      댓글이 추가될 게시글의 ID
   * @param commentDTO  추가할 댓글의 정보
   * @param userDetails 인증된 사용자 정보
   * @return 성공 시 201(Created) 상태 코드와 추가된 댓글의 정보를 반환
   */
  @PostMapping
  public ResponseEntity<CommentDTO> addComment(@PathVariable Long postId,
      @Valid @RequestBody CommentDTO commentDTO,
      @AuthenticationPrincipal UserDetails userDetails) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(
            commentService.addComment(postId, userDetails.getUsername(), commentDTO.getContent()));
  }

  /**
   * 댓글 목록 조회 API
   *
   * @param postId   댓글을 조회할 게시글의 ID
   * @param pageable 페이징 및 정렬 정보
   * @return 페이징 처리된 댓글 목록을 포함한 200(OK) 상태 코드 반환
   */
  @GetMapping
  public ResponseEntity<Page<CommentDTO>> getComments(@PathVariable Long postId,
      @PageableDefault(sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {

    return ResponseEntity.ok(commentService.findByPostId(postId, pageable));
  }


  @PutMapping("/{commentId}")
  public ResponseEntity<CommentDTO> updateComment(
      @PathVariable Long postId,
      @PathVariable Long commentId,
      @Valid @RequestBody UpdateCommentDTO updateCommentDTO,
      @AuthenticationPrincipal UserDetails userDetails) {
    return ResponseEntity.ok(
        commentService.updateComment(commentId, postId, userDetails.getUsername(), updateCommentDTO));
  }

  /**
   * 댓글 삭제 API
   *
   * @param postId      댓글이 속한 게시글의 ID
   * @param commentId   삭제할 댓글의 ID
   * @param userDetails 인증된 사용자 정보
   * @return 성공 시 204(No Content) 상태 코드 반환
   */
  @DeleteMapping("/{commentId}")
  public ResponseEntity<Void> deleteComment(@PathVariable Long postId,
      @PathVariable Long commentId,
      @AuthenticationPrincipal UserDetails userDetails) {
    commentService.deleteComment(postId, commentId, userDetails.getUsername());
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}
