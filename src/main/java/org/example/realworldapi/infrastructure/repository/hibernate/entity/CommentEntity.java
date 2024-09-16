package org.example.realworldapi.infrastructure.repository.hibernate.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.realworldapi.domain.model.comment.Comment;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "COMMENTS")
public class CommentEntity {

  @Id private UUID id;

  @CreationTimestamp private LocalDateTime createdAt;
  @UpdateTimestamp private LocalDateTime updatedAt;

  private String body;

  @ManyToOne
  @JoinColumn(name = "article_id", referencedColumnName = "id", nullable = false)
  private ArticleEntity article;

  @ManyToOne
  @JoinColumn(name = "author_id", referencedColumnName = "id", nullable = false)
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
