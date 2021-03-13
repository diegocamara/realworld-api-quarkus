package org.example.realworldapi.domain.feature;

import java.util.UUID;

public interface DeleteArticleBySlug {
  void handle(UUID authorId, String slug);
}
