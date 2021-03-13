package org.example.realworldapi.application.web.model.response;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@RegisterForReflection
public class ArticlesResponse {

  private List<ArticleResponse> articles;
  private long articlesCount;

  public ArticlesResponse(List<ArticleResponse> articles, long articlesCount) {
    this.articles = articles;
    this.articlesCount = articlesCount;
  }
}
