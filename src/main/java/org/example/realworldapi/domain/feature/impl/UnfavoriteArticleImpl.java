package org.example.realworldapi.domain.feature.impl;

import lombok.AllArgsConstructor;
import org.example.realworldapi.domain.feature.FindArticleBySlug;
import org.example.realworldapi.domain.feature.UnfavoriteArticle;
import org.example.realworldapi.domain.model.article.FavoriteRelationshipRepository;

import java.util.UUID;

@AllArgsConstructor
public class UnfavoriteArticleImpl implements UnfavoriteArticle {

  private final FindArticleBySlug findArticleBySlug;
  private final FavoriteRelationshipRepository favoriteRelationshipRepository;

  @Override
  public void handle(String articleSlug, UUID currentUserId) {
    final var article = findArticleBySlug.handle(articleSlug);
    final var favoriteRelationshipOptional =
        favoriteRelationshipRepository.findByArticleIdAndUserId(article.getId(), currentUserId);
    favoriteRelationshipOptional.ifPresent(favoriteRelationshipRepository::delete);
  }
}
