package org.example.realworldapi.application.web.model.response;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Getter;
import lombok.Setter;
import org.example.realworldapi.application.data.CommentData;

import java.util.List;

@Getter
@Setter
@RegisterForReflection
public class CommentsResponse {

  private List<CommentResponse> comments;

  public CommentsResponse(List<CommentData> comments) {
    //    this.comments = comments.stream().map(CommentResponse::new).collect(Collectors.toList());
  }
}
