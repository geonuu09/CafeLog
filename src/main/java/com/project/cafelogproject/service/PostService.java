package com.project.cafelogproject.service;

import com.project.cafelogproject.config.exception.CustomException;
import com.project.cafelogproject.config.exception.ErrorCode;
import com.project.cafelogproject.domain.Post;
import com.project.cafelogproject.domain.Tag;
import com.project.cafelogproject.domain.User;
import com.project.cafelogproject.dto.AddPostRequestDTO;
import com.project.cafelogproject.dto.PostDetailResponseDTO;
import com.project.cafelogproject.dto.PostResponseDTO;
import com.project.cafelogproject.repository.PostRepository;
import com.project.cafelogproject.repository.TagRepository;
import com.project.cafelogproject.repository.UserRepository;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {
  private final PostRepository postRepository;
  private final UserRepository userRepository;
  private final TagRepository tagRepository;


  @Transactional
  public PostResponseDTO addPost(AddPostRequestDTO request, String userEmail) {
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

    return toPostResponseDTO(savedPost);
  }

  public List<PostResponseDTO> searchPosts(String query) {
    List<Post> posts = postRepository.findByTagsNameContainingIgnoreCaseOrCafeNameContainingIgnoreCase(query, query);

    if (posts.isEmpty()) {
      throw new CustomException(ErrorCode.POST_NOT_FOUND);
    }

    return posts.stream().map(this::toPostResponseDTO).collect(Collectors.toList());
  }
  // post 객체를 DTO 로 변환
  private PostResponseDTO toPostResponseDTO(Post post) {
    return PostResponseDTO.builder()
        .id(post.getId())
        .cafeName(post.getCafeName())
        .address(post.getAddress())
        .recommend(post.getRecommend())
        .content(post.getContent())
        .isPublic(post.getIsPublic())
        .userEmail(post.getUser().getEmail())  // 작성자 이메일 추가
        .tags(post.getTags().stream().map(Tag::getName).collect(Collectors.toList()))
        .build();
  }

  public PostDetailResponseDTO getPostById(Long id) {
    Post post = postRepository.findById(id)
        .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
    return toPostDetailResponseDTO(post);
  }
  private PostDetailResponseDTO toPostDetailResponseDTO(Post post) {
    return PostDetailResponseDTO.builder()
        .id(post.getId())
        .cafeName(post.getCafeName())
        .content(post.getContent())
        .authorName(post.getUser().getNickname())  // 작성자 닉네임
        .createdDate(post.getCreatedDate())
        .tags(post.getTags().stream().map(Tag::getName).collect(Collectors.toList()))
        .build();
  }


}