package org.example.realworldapi.web.model.response;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.realworldapi.domain.entity.Article;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@RegisterForReflection
public class ArticlesResponse {

  private List<ArticleResponse> articles;

  public int getArticlesCount() {
    return this.articles.size();
  }

  public ArticlesResponse(List<Article> articles) {
    this.articles = articles.stream().map(ArticleResponse::new).collect(Collectors.toList());
  }
}
