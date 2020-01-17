package org.example.realworldapi.infrastructure.web.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonRootName;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.realworldapi.application.data.CommentData;
import org.example.realworldapi.application.data.ProfileData;

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
  private ProfileData author;

  public CommentResponse(CommentData commentData) {
    this.id = commentData.getId();
    this.createdAt = commentData.getCreatedAt();
    this.updatedAt = commentData.getUpdatedAt();
    this.body = commentData.getBody();
    this.author = commentData.getAuthor();
  }
}
