package com.project.cafelogproject.service;

import com.project.cafelogproject.config.exception.CustomException;
import com.project.cafelogproject.config.exception.ErrorCode;
import com.project.cafelogproject.domain.Comment;
import com.project.cafelogproject.domain.Post;
import com.project.cafelogproject.domain.User;
import com.project.cafelogproject.dto.CommentDTO;
import com.project.cafelogproject.dto.UpdateCommentDTO;
import com.project.cafelogproject.repository.CommentRepository;
import com.project.cafelogproject.repository.PostRepository;
import com.project.cafelogproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {
  private final CommentRepository commentRepository;
  private final PostRepository postRepository;
  private final UserRepository userRepository;



  @Transactional
  public CommentDTO addComment(Long postId, String userEmail, String content) {
    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
    User user = userRepository.findByEmail(userEmail)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    Comment comment = Comment.builder()
        .post(post)
        .user(user)
        .content(content)
        .build();

    return toCommentDTO(commentRepository.save(comment));
  }


  public Page<CommentDTO> findByPostId(Long postId, Pageable pageable) {
    return commentRepository.findByPostId(postId, pageable)
        .map(this::toCommentDTO);
  }


  @Transactional
  public CommentDTO updateComment(Long commentId, Long postId, String userEmail, UpdateCommentDTO updateCommentDTO) {
    Comment comment = commentRepository.findById(commentId)
        .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

    if (!comment.getPost().getId().equals(postId)) {
      throw new CustomException(ErrorCode.INVALID_POST_ID);
    }

    if (!comment.getUser().getEmail().equals(userEmail)) {
      throw new CustomException(ErrorCode.UNAUTHORIZED_COMMENT_UPDATE);
    }

    comment.updateContent(updateCommentDTO.getContent());
    Comment updatedComment = commentRepository.save(comment);

    return toCommentDTO(updatedComment);
  }
  @Transactional
  public void deleteComment(Long postId, Long commentId, String userEmail) {
    Comment comment = commentRepository.findById(commentId)
        .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

    if(!comment.getPost().getId().equals(postId)) {
      throw new CustomException(ErrorCode.INVALID_POST_ID);
    }
    if (!comment.getUser().getEmail().equals(userEmail)) {
      throw new CustomException(ErrorCode.UNAUTHORIZED_COMMENT_DELETE);
    }

    commentRepository.delete(comment);
  }



  private CommentDTO toCommentDTO(Comment comment) {
    return CommentDTO.builder()
        .id(comment.getId())
        .content(comment.getContent())
        .userEmail(comment.getUser().getEmail())
        .createdDate(comment.getCreatedDate())
        .build();
  }
}