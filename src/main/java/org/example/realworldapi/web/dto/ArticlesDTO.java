package org.example.realworldapi.web.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.realworldapi.domain.entity.persistent.Article;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@RegisterForReflection
public class ArticlesDTO {

  private List<ArticleDTO> articles;

  public int articlesCount() {
    return this.articles.size();
  }

  public ArticlesDTO(List<Article> articles) {
    this.articles = articles.stream().map(article -> new ArticleDTO()).collect(Collectors.toList());
  }
}
