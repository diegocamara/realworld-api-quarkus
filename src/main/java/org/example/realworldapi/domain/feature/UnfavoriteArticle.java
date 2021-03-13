package org.example.realworldapi.domain.feature;

import java.util.UUID;

public interface UnfavoriteArticle {
  void handle(String articleSlug, UUID currentUserId);
}
