package org.example.realworldapi.domain.feature;

import org.example.realworldapi.domain.model.article.Article;
import org.example.realworldapi.domain.model.article.ArticleFilter;
import org.example.realworldapi.domain.model.article.PageResult;

public interface FindArticlesByFilter {
  PageResult<Article> handle(ArticleFilter articleFilter);
}
