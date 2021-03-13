package org.example.realworldapi.domain.feature.impl;

import lombok.AllArgsConstructor;
import org.example.realworldapi.domain.feature.FindArticleBySlug;
import org.example.realworldapi.domain.model.article.Article;
import org.example.realworldapi.domain.model.article.ArticleRepository;
import org.example.realworldapi.domain.exception.ArticleNotFoundException;

@AllArgsConstructor
public class FindArticleBySlugImpl implements FindArticleBySlug {

  private final ArticleRepository articleRepository;

  @Override
  public Article handle(String slug) {
    return articleRepository.findBySlug(slug).orElseThrow(ArticleNotFoundException::new);
  }
}
