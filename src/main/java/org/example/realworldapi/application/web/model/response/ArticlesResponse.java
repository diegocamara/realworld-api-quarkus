package org.example.realworldapi.application.web.model.response;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.realworldapi.application.data.ArticlesData;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@RegisterForReflection
public class ArticlesResponse {

  private List<ArticleResponse> articles;
  private long articlesCount;

  public ArticlesResponse(ArticlesData result) {
    //    this.articles =
    //        result.getArticles().stream().map(ArticleResponse::new).collect(Collectors.toList());
    this.articlesCount = result.getArticlesCount();
  }
}
