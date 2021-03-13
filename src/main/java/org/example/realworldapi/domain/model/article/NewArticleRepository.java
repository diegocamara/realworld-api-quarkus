package org.example.realworldapi.domain.model.article;

import java.util.Optional;
import java.util.UUID;

public interface NewArticleRepository {
  boolean existsBySlug(String slug);

  void save(Article article);

  Optional<Article> findArticleById(UUID id);
}
