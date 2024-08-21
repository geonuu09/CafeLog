package com.project.cafelogproject.service;

import com.project.cafelogproject.config.exception.CustomException;
import com.project.cafelogproject.config.exception.ErrorCode;
import com.project.cafelogproject.domain.Post;
import com.project.cafelogproject.domain.Tag;
import com.project.cafelogproject.domain.User;
import com.project.cafelogproject.dto.AddPostRequestDTO;
import com.project.cafelogproject.dto.PostDetailDTO;
import com.project.cafelogproject.dto.PostResponseDTO;
import com.project.cafelogproject.dto.UpdatePostRequestDTO;
import com.project.cafelogproject.repository.PostRepository;
import com.project.cafelogproject.repository.UserRepository;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

  private final PostRepository postRepository;
  private final UserRepository userRepository;
  private final TagService tagService;

  @Transactional
  public PostDetailDTO addPost(AddPostRequestDTO request, String userEmail) {
    User user = userRepository.findByEmail(userEmail)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    Set<Tag> tags = request.getTags().stream()
        .map(tagService::getOrCreateTag)
        .collect(Collectors.toSet());

    Post post = Post.builder()
        .cafeName(request.getCafeName())
        .address(request.getAddress())
        .recommend(request.getRecommend())
        .content(request.getContent())
        .isPublic(request.getIsPublic())
        .user(user)
        .tags(tags)
        .build();

    Post savedPost = postRepository.save(post);

    return toPostDetailDTO(savedPost);
  }

  @Transactional
  public PostResponseDTO updatePost(Long postId, UpdatePostRequestDTO updateDTO, String userEmail) {
    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

    User user = userRepository.findByEmail(userEmail)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    if (!post.getUser().equals(user)) {
      throw new CustomException(ErrorCode.UNAUTHORIZED_POST_UPDATE);
    }


    post.update(updateDTO.getCafeName(),
        updateDTO.getAddress(),
        updateDTO.getRecommend(),
        updateDTO.getContent(),
        updateDTO.getIsPublic());

    updatePostTags(post, updateDTO.getTags());

    Post updatedPost = postRepository.save(post);
    return toPostResponseDTO(updatedPost);
  }
  private void updatePostTags(Post post, List<String> newTags) {
    post.getTags().clear();
    Set<Tag> updatedTags = newTags.stream()
        .map(tagService::getOrCreateTag)
        .collect(Collectors.toSet());
    post.setTags(updatedTags);
  }

  public Page<PostDetailDTO> searchPosts(String query, Pageable pageable) {
    Page<Post> posts = postRepository.findByTagsNameContainingIgnoreCaseOrCafeNameContainingIgnoreCase(
        query, query, pageable);

    if (posts.isEmpty()) {
      throw new CustomException(ErrorCode.POST_NOT_FOUND);
    }

    return posts.map(this::toPostDetailDTO);
  }

  public Page<PostDetailDTO> getAllPosts(Pageable pageable) {
    return postRepository.findAll(pageable).map(this::toPostDetailDTO);
  }

  @Transactional
  public void deletePost(Long postId, String userEmail) {
    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

    User user = userRepository.findByEmail(userEmail)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    if (!post.getUser().equals(user)) {
      throw new CustomException(ErrorCode.UNAUTHORIZED_POST_DELETE);
    }

    postRepository.delete(post);
  }

  private PostDetailDTO toPostDetailDTO(Post post) {
    return PostDetailDTO.builder()
        .cafeName(post.getCafeName())
        .address(post.getAddress())
        .recommend(post.getRecommend())
        .content(post.getContent())
        .userEmail(post.getUser().getEmail())
        .createdDate(post.getCreatedDate())
        .tags(post.getTags().stream().map(Tag::getName).collect(Collectors.toList()))
        .build();
  }

  private PostResponseDTO toPostResponseDTO(Post post) {
    return PostResponseDTO.builder()
        .id(post.getId())
        .cafeName(post.getCafeName())
        .address(post.getAddress())
        .recommend(post.getRecommend())
        .content(post.getContent())
        .isPublic(post.getIsPublic())
        .userEmail(post.getUser().getEmail())
        .createdDate(post.getCreatedDate())
        .tags(post.getTags().stream().map(Tag::getName).collect(Collectors.toList()))
        .build();
  }
}