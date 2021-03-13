package org.example.realworldapi.domain.feature.impl;

import lombok.AllArgsConstructor;
import org.example.realworldapi.domain.feature.FindArticleById;
import org.example.realworldapi.domain.model.article.Article;
import org.example.realworldapi.domain.model.article.ArticleRepository;
import org.example.realworldapi.domain.exception.ArticleNotFoundException;

import java.util.UUID;

@AllArgsConstructor
public class FindArticleByIdImpl implements FindArticleById {

  private final ArticleRepository articleRepository;

  @Override
  public Article handle(UUID id) {
    return articleRepository.findArticleById(id).orElseThrow(ArticleNotFoundException::new);
  }
}
