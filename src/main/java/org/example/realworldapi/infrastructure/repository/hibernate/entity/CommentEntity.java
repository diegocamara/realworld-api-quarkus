package org.example.realworldapi.infrastructure.repository.hibernate.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.realworldapi.domain.model.comment.Comment;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "NEWCOMMENTS")
public class CommentEntity {

  @Id private UUID id;

  @CreationTimestamp private LocalDateTime createdAt;
  @UpdateTimestamp private LocalDateTime updatedAt;

  private String body;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  private ArticleEntity article;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  private UserEntity author;

  public CommentEntity(UserEntity author, ArticleEntity article, Comment comment) {
    this.id = comment.getId();
    this.body = comment.getBody();
    this.createdAt = comment.getCreatedAt();
    this.updatedAt = comment.getUpdatedAt();
    this.article = article;
    this.author = author;
  }
}
