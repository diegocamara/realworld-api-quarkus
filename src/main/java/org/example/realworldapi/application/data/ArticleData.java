package org.example.realworldapi.application.data;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@RegisterForReflection
public class ArticleData {

  private String slug;
  private String title;
  private String description;
  private String body;
  private List<String> tagList;
  private boolean favorited;
  private long favoritesCount;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private ProfileData author;
}
