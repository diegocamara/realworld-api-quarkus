package org.example.realworldapi.application.web.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonRootName;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.realworldapi.domain.model.article.Article;
import org.example.realworldapi.domain.model.tag.Tag;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
  private ProfileResponse author;

  public ArticleResponse(
      Article article, ProfileResponse author, long favoritesCount, List<Tag> tags) {
    this.slug = article.getSlug();
    this.title = article.getTitle();
    this.description = article.getDescription();
    this.body = article.getBody();
    this.createdAt = article.getCreatedAt();
    this.updatedAt = article.getUpdatedAt();
    this.author = author;
    this.favoritesCount = favoritesCount;
    this.tagList = tags.stream().map(Tag::getName).collect(Collectors.toList());
  }
}
