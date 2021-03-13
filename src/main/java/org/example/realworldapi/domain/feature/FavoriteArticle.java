package org.example.realworldapi.domain.feature;

import org.example.realworldapi.domain.model.article.FavoriteRelationship;

import java.util.UUID;

public interface FavoriteArticle {
  FavoriteRelationship handle(String articleSlug, UUID currentUserId);
}
