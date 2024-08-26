package com.project.cafelogproject.service;

import com.project.cafelogproject.config.exception.CustomException;
import com.project.cafelogproject.config.exception.ErrorCode;
import com.project.cafelogproject.domain.Favorite;
import com.project.cafelogproject.domain.Post;
import com.project.cafelogproject.domain.Tag;
import com.project.cafelogproject.domain.User;
import com.project.cafelogproject.dto.FavoriteWithPostSummaryDTO;
import com.project.cafelogproject.dto.PostSummaryDTO;
import com.project.cafelogproject.repository.FavoriteRepository;
import com.project.cafelogproject.repository.PostRepository;
import com.project.cafelogproject.repository.UserRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class FavoriteService {

  private final FavoriteRepository favoriteRepository;
  private final UserRepository userRepository;
  private final PostRepository postRepository;


  public void addFavorite(String userEmail, Long postId) {
    User user = userRepository.findByEmail(userEmail)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

    if (favoriteRepository.existsByUserAndPost(user, post)) {
      throw new CustomException(ErrorCode.FAVORITE_ALREADY_EXISTS);
    }
    favoriteRepository.save(new Favorite(user, post));
  }

  public List<FavoriteWithPostSummaryDTO> getAllFavorites(String userEmail, Pageable pageable) {
    User user = userRepository.findByEmail(userEmail)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    return favoriteRepository.findAllByUser(user, pageable).stream()
        .map(this::toFavoriteWithPostSummaryDTO)
        .collect(Collectors.toList());
  }

  private FavoriteWithPostSummaryDTO toFavoriteWithPostSummaryDTO(Favorite favorite) {
    Post post = favorite.getPost();
    PostSummaryDTO postSummary = new PostSummaryDTO(
        post.getId(),
        post.getCafeName(),
        post.getTags().stream()
            .map(Tag::getName)
            .collect(Collectors.toList())
    );
    return new FavoriteWithPostSummaryDTO(
        favorite.getId(),
        favorite.getCreatedDate(),
        postSummary
    );
  }

  public void removeFavorite(String userEmail, Long postId) {
    User user = userRepository.findByEmail(userEmail)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

    favoriteRepository.deleteByUserAndPost(user,post);
  }



}
