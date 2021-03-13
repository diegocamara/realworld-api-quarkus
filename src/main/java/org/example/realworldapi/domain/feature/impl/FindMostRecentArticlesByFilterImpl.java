package org.example.realworldapi.domain.feature.impl;

import lombok.AllArgsConstructor;
import org.example.realworldapi.domain.feature.FindMostRecentArticlesByFilter;
import org.example.realworldapi.domain.model.article.Article;
import org.example.realworldapi.domain.model.article.ArticleFilter;
import org.example.realworldapi.domain.model.article.ArticleRepository;
import org.example.realworldapi.domain.model.article.PageResult;

@AllArgsConstructor
public class FindMostRecentArticlesByFilterImpl implements FindMostRecentArticlesByFilter {

  private final ArticleRepository articleRepository;

  @Override
  public PageResult<Article> handle(ArticleFilter articleFilter) {
    return articleRepository.findMostRecentArticlesByFilter(articleFilter);
  }
}
