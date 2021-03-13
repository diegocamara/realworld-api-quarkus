package org.example.realworldapi.domain.model.comment;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class NewCommentInput {
  private UUID authorId;
  private String articleSlug;
  private String body;
}
