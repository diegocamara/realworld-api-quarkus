package org.example.realworldapi.infrastructure.web.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonRootName;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.realworldapi.domain.model.entity.Comment;
import org.example.realworldapi.domain.model.entity.Profile;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@JsonRootName("comment")
@RegisterForReflection
public class CommentResponse {

  private Long id;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
  private LocalDateTime createdAt;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
  private LocalDateTime updatedAt;

  private String body;
  private Profile author;

  public CommentResponse(Comment comment) {
    this.id = comment.getId();
    this.createdAt = comment.getCreatedAt();
    this.updatedAt = comment.getUpdatedAt();
    this.body = comment.getBody();
    this.author = comment.getAuthor();
  }
}
