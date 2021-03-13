package org.example.realworldapi.domain.feature.impl;

import lombok.AllArgsConstructor;
import org.example.realworldapi.domain.feature.IsArticleFavorited;
import org.example.realworldapi.domain.model.article.Article;
import org.example.realworldapi.domain.model.article.FavoriteRelationshipRepository;

import java.util.UUID;

@AllArgsConstructor
public class IsArticleFavoritedImpl implements IsArticleFavorited {

  private final FavoriteRelationshipRepository favoriteRelationshipRepository;

  @Override
  public boolean handle(Article article, UUID currentUserId) {
    return favoriteRelationshipRepository.isFavorited(article, currentUserId);
  }
}
