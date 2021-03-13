package org.example.realworldapi.domain.feature;

import org.example.realworldapi.domain.model.article.Article;

import java.util.UUID;

public interface FindArticleByAuthorAndSlug {
  Article handle(UUID authorId, String slug);
}
