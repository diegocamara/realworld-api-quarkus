package org.example.realworldapi.domain.model.builder;

import lombok.Getter;
import lombok.Setter;
import org.example.realworldapi.domain.model.entity.Article;
import org.example.realworldapi.domain.model.entity.User;

@Getter
@Setter
public class ArticleBuilder {

  private Long id;
  private String slug;
  private String title;
  private String description;
  private String body;
  private User author;

  public ArticleBuilder id(Long id) {
    this.id = id;
    return this;
  }

  public ArticleBuilder slug(String slug) {
    this.slug = slug;
    return this;
  }

  public ArticleBuilder title(String title) {
    this.title = title;
    return this;
  }

  public ArticleBuilder description(String description) {
    this.description = description;
    return this;
  }

  public ArticleBuilder body(String body) {
    this.body = body;
    return this;
  }

  public ArticleBuilder author(User author) {
    this.author = author;
    return this;
  }

  public Article build() {
    return new Article(
        this.id,
        this.slug,
        this.title,
        this.description,
        this.body,
        null,
        null,
        this.author,
        null,
        null,
        null);
  }
}
