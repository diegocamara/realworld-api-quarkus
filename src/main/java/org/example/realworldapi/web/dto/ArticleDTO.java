package org.example.realworldapi.web.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.realworldapi.domain.entity.Profile;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@RegisterForReflection
public class ArticleDTO {

  private String slug;
  private String title;
  private String description;
  private String body;
  private List<String> tags;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private boolean favorited;
  private int favoritesCount;
  private Profile author;
}
