package org.example.realworldapi.infrastructure.web.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonRootName;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.realworldapi.domain.model.entity.Article;
import org.example.realworldapi.domain.model.entity.Profile;

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
  private int favoritesCount;
  private Profile author;

  public ArticleResponse(Article article) {
    this.slug = article.getSlug();
    this.title = article.getTitle();
    this.description = article.getDescription();
    this.body = article.getBody();
    this.tagList = article.getTagList();
    this.createdAt = article.getCreatedAt();
    this.updatedAt = article.getUpdatedAt();
    this.favorited = article.isFavorited();
    this.favoritesCount = article.getFavoritesCount();
    this.author = article.getAuthor();
  }
}
