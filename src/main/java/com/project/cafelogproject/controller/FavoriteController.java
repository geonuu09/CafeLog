package com.project.cafelogproject.controller;

import com.project.cafelogproject.dto.FavoriteWithPostSummaryDTO;
import com.project.cafelogproject.service.FavoriteService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/favorites")
@RequiredArgsConstructor
public class FavoriteController {
  private final FavoriteService favoriteService;

  @PostMapping("/{postId}")
  public ResponseEntity<Void> addFavorite(@PathVariable Long postId, @AuthenticationPrincipal UserDetails userDetails) {
    favoriteService.addFavorite(userDetails.getUsername(), postId);
    return ResponseEntity.ok().build();
  }

  @GetMapping
  public ResponseEntity<List<FavoriteWithPostSummaryDTO>> getFavorites(@AuthenticationPrincipal UserDetails userDetails,
      @PageableDefault(sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {
    List<FavoriteWithPostSummaryDTO> favorites = favoriteService.getAllFavorites(userDetails.getUsername(),pageable);
    return ResponseEntity.ok(favorites);
  }

  @DeleteMapping("/{postId}")
  public ResponseEntity<Void> deleteFavorite(@PathVariable Long postId, @AuthenticationPrincipal UserDetails userDetails) {
    favoriteService.removeFavorite(userDetails.getUsername(), postId);
    return ResponseEntity.ok().build();
  }
}
