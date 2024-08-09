package com.project.cafelogproject.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Post")
@Getter
@Setter
@NoArgsConstructor

public class Post {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "post_id")
  private Long id;

  @Column(name = "cafe_name")
  private String name;

  @Column(name = "cafe_address")
  private String address;

  @Column(name = "cafe_recommend")
  private Boolean recommend;

  @Column(name = "cafe_description")
  @Lob
  private String description;

  @Column(name = "post_public")
  private Boolean isPublic;

  @Column(name = "post_created_date")
  private LocalDateTime createdDate;

  @Column(name = "post_modified_date")
  private LocalDateTime modifiedDate;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @Builder
  public Post(String name, String address, Boolean recommend, String description, Boolean isPublic, User user) {
    this.name = name;
    this.address = address;
    this.recommend = recommend;
    this.description = description;
    this.isPublic = isPublic;
    this.user = user;
    this.createdDate = LocalDateTime.now();
    this.modifiedDate = LocalDateTime.now();
  }

  public void update(String name, String address, Boolean recommend, String description, Boolean isPublic) {
    this.name = name;
    this.address = address;
    this.recommend = recommend;
    this.description = description;
    this.isPublic = isPublic;
    this.modifiedDate = LocalDateTime.now();
  }
}