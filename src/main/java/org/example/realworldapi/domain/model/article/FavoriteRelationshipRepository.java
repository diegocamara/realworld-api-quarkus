package org.example.realworldapi.domain.model.article;

import java.util.UUID;

public interface FavoriteRelationshipRepository {
  boolean isFavorited(Article article, UUID currentUserId);

  long favoritesCount(Article article);
}
