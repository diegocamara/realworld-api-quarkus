package org.example.realworldapi.domain.model.article;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class NewArticleInput {
  private UUID authorId;
  private String title;
  private String description;
  private String body;
  private List<String> tagList;
}
