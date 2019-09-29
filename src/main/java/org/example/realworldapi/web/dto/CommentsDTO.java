package org.example.realworldapi.web.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Getter;
import lombok.Setter;
import org.example.realworldapi.domain.entity.Comment;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@RegisterForReflection
public class CommentsDTO {

  private List<CommentDTO> comments;

  public CommentsDTO(List<Comment> comments) {
    this.comments = comments.stream().map(CommentDTO::new).collect(Collectors.toList());
  }
}
