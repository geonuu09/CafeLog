package com.project.cafelogproject.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "Post")
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)

public class Post {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "post_id")
  private Long id;

  @Column(name = "cafe_name")
  private String cafeName;

  @Column(name = "cafe_address")
  private String address;

  @Column(name = "cafe_recommend")
  private Boolean recommend;

  @Column(name = "cafe_content")
  private String content;

  @Column(name = "post_public")
  private Boolean isPublic;

  @Column(name = "post_created_date")
  @CreatedDate
  private LocalDateTime createdDate;

  @Column(name = "post_modified_date")
  @LastModifiedDate
  private LocalDateTime modifiedDate;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @Builder
  public Post(String cafeName, String address, Boolean recommend, String content, Boolean isPublic, User user) {
    this.cafeName = cafeName;
    this.address = address;
    this.recommend = recommend;
    this.content = content;
    this.isPublic = isPublic;
    this.user = user;
    this.createdDate = LocalDateTime.now();
    this.modifiedDate = LocalDateTime.now();
  }

  public void update(String cafeName, String address, Boolean recommend, String content, Boolean isPublic) {
    this.cafeName = cafeName;
    this.address = address;
    this.recommend = recommend;
    this.content = content;
    this.isPublic = isPublic;
    this.modifiedDate = LocalDateTime.now();
  }
}