package org.example.realworldapi.web.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.realworldapi.domain.entity.Article;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@RegisterForReflection
public class ArticlesDTO {

  private List<Article> articles;

  public int getArticlesCount() {
    return this.articles.size();
  }

  public ArticlesDTO(List<Article> articles) {
    this.articles = articles;
  }
}
