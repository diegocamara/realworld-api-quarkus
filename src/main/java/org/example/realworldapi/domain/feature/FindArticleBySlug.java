package org.example.realworldapi.domain.feature;

import org.example.realworldapi.domain.model.article.Article;

public interface FindArticleBySlug {
  Article handle(String slug);
}
