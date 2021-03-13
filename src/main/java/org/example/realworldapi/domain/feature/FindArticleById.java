package org.example.realworldapi.domain.feature;

import org.example.realworldapi.domain.model.article.Article;

import java.util.UUID;

public interface FindArticleById {
  Article handle(UUID id);
}
