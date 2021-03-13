package org.example.realworldapi.domain.feature;

import java.util.UUID;

public interface ArticleFavoritesCount {
  long handle(UUID articleId);
}
