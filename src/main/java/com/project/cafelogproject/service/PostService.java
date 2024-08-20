package com.project.cafelogproject.service;

import com.project.cafelogproject.config.exception.CustomException;
import com.project.cafelogproject.config.exception.ErrorCode;
import com.project.cafelogproject.domain.Post;
import com.project.cafelogproject.domain.Tag;
import com.project.cafelogproject.domain.User;
import com.project.cafelogproject.dto.AddPostRequestDTO;
import com.project.cafelogproject.dto.PostDetailDTO;
import com.project.cafelogproject.repository.PostRepository;
import com.project.cafelogproject.repository.TagRepository;
import com.project.cafelogproject.repository.UserRepository;
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
  private final TagRepository tagRepository;

  @Transactional
  public PostDetailDTO addPost(AddPostRequestDTO request, String userEmail) {
    User user = userRepository.findByEmail(userEmail)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    Set<Tag> tags = request.getTags().stream()
        .map(tagName -> tagRepository.findByName(tagName)
            .orElseGet(() -> tagRepository.save(new Tag(tagName))))
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

  public PostDetailDTO getPostById(Long id) {
    Post post = postRepository.findById(id)
        .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
    return toPostDetailDTO(post);
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
}
