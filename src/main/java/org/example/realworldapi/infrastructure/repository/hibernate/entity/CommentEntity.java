package org.example.realworldapi.infrastructure.repository.hibernate.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
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
}
