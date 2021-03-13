package org.example.realworldapi.domain.feature.impl;

import lombok.AllArgsConstructor;
import org.example.realworldapi.domain.feature.DeleteArticleBySlug;
import org.example.realworldapi.domain.feature.FindArticleByAuthorAndSlug;
import org.example.realworldapi.domain.model.article.ArticleRepository;

import java.util.UUID;

@AllArgsConstructor
public class DeleteArticleBySlugImpl implements DeleteArticleBySlug {

  private final FindArticleByAuthorAndSlug findArticleByAuthorAndSlug;
  private final ArticleRepository articleRepository;

  @Override
  public void handle(UUID authorId, String slug) {
    final var article = findArticleByAuthorAndSlug.handle(authorId, slug);
    articleRepository.delete(article);
  }
}
