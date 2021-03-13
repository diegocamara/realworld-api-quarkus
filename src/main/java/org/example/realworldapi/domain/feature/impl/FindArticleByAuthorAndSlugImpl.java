package org.example.realworldapi.domain.feature.impl;

import lombok.AllArgsConstructor;
import org.example.realworldapi.domain.feature.FindArticleByAuthorAndSlug;
import org.example.realworldapi.domain.model.article.Article;
import org.example.realworldapi.domain.model.article.NewArticleRepository;
import org.example.realworldapi.domain.model.exception.ArticleNotFoundException;

import java.util.UUID;

@AllArgsConstructor
public class FindArticleByAuthorAndSlugImpl implements FindArticleByAuthorAndSlug {

  private final NewArticleRepository articleRepository;

  @Override
  public Article handle(UUID authorId, String slug) {
    return articleRepository
        .findByAuthorAndSlug(authorId, slug)
        .orElseThrow(ArticleNotFoundException::new);
  }
}
