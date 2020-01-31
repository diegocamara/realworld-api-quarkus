package org.example.realworldapi.infrastructure.web.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonRootName;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.realworldapi.application.data.ArticleData;
import org.example.realworldapi.application.data.ProfileData;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@JsonRootName("article")
@RegisterForReflection
public class ArticleResponse {

  private String slug;
  private String title;
  private String description;
  private String body;
  private List<String> tagList;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
  private LocalDateTime createdAt;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
  private LocalDateTime updatedAt;

  private boolean favorited;
  private long favoritesCount;
  private ProfileData author;

  public ArticleResponse(ArticleData articleData) {
    this.slug = articleData.getSlug();
    this.title = articleData.getTitle();
    this.description = articleData.getDescription();
    this.body = articleData.getBody();
    this.tagList = articleData.getTagList();
    this.createdAt = articleData.getCreatedAt();
    this.updatedAt = articleData.getUpdatedAt();
    this.favorited = articleData.isFavorited();
    this.favoritesCount = articleData.getFavoritesCount();
    this.author = articleData.getAuthor();
  }
}
