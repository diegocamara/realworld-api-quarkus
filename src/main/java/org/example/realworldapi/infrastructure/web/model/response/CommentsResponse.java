package org.example.realworldapi.infrastructure.web.model.response;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Getter;
import lombok.Setter;
import org.example.realworldapi.application.data.CommentData;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@RegisterForReflection
public class CommentsResponse {

  private List<CommentResponse> comments;

  public CommentsResponse(List<CommentData> comments) {
    this.comments = comments.stream().map(CommentResponse::new).collect(Collectors.toList());
  }
}
