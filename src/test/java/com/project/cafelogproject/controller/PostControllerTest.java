package com.project.cafelogproject.controller;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.cafelogproject.domain.Post;
import com.project.cafelogproject.domain.User;
import com.project.cafelogproject.dto.AddPostRequestDTO;
import com.project.cafelogproject.dto.UpdatePostRequestDTO;
import com.project.cafelogproject.repository.PostRepository;
import com.project.cafelogproject.repository.UserRepository;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class PostControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private PostRepository postRepository;

  @Autowired
  private UserRepository userRepository;

  @BeforeEach
  void setUp() {
    postRepository.deleteAll();
    userRepository.deleteAll();
  }

  @Test
  @WithMockUser(username = "test@example.com")
  public void testAddPost() throws Exception {
    User user = new User(
        "test@example.com",
        "password",
        "Test User");

    userRepository.save(user);

    AddPostRequestDTO requestDTO = new AddPostRequestDTO();

    requestDTO.setCafeName("Test Cafe");
    requestDTO.setAddress("Test Address");
    requestDTO.setRecommend(true);
    requestDTO.setContent("Test Content");
    requestDTO.setIsPublic(true);
    requestDTO.setTags(Arrays.asList("tag1", "tag2"));

    mockMvc.perform(post("/posts/write")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestDTO)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.cafeName").value("Test Cafe"))
        .andExpect(jsonPath("$.tags", hasSize(2)))
        .andExpect(jsonPath("$.tags", containsInAnyOrder("tag1", "tag2")));
  }

  @Test
  @WithMockUser(username = "test@example.com")
  public void testUpdatePost() throws Exception {
    User user = new User("test@example.com", "password", "Test User");
    userRepository.save(user);

    Post post = new Post();
    post.setCafeName("Original Cafe");
    post.setAddress("Original Address");
    post.setRecommend(true);
    post.setContent("Original Content");
    post.setIsPublic(true);
    post.setUser(user);
    post = postRepository.save(post);

    UpdatePostRequestDTO updateDTO = new UpdatePostRequestDTO();
    updateDTO.setCafeName("Updated Cafe");
    updateDTO.setAddress("Updated Address");
    updateDTO.setRecommend(false);
    updateDTO.setContent("Updated Content");
    updateDTO.setIsPublic(true);
    updateDTO.setTags(Arrays.asList("updatedTag1", "updatedTag2"));

    mockMvc.perform(put("/posts/" + post.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updateDTO)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.cafeName").value("Updated Cafe"))
        .andExpect(jsonPath("$.content").value("Updated Content"))
        .andExpect(jsonPath("$.tags", hasSize(2)))
        .andExpect(jsonPath("$.tags", containsInAnyOrder("updatedTag1", "updatedTag2")));
  }



  @Test
  @WithMockUser(username = "test@example.com")
  public void testDeletePost() throws Exception {
    User user = new User("test@example.com", "password", "Test User");
    userRepository.save(user);

    Post post = new Post();
    post.setCafeName("Test Cafe");
    post.setAddress("Test Address");
    post.setRecommend(true);
    post.setContent("Test Content");
    post.setIsPublic(true);
    post.setUser(user);
    post = postRepository.save(post);

    mockMvc.perform(delete("/posts/" + post.getId()))
        .andExpect(status().isNoContent());

    assertFalse(postRepository.findById(post.getId()).isPresent());
  }
}