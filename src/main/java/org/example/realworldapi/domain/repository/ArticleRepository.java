package org.example.realworldapi.domain.repository;

import org.example.realworldapi.domain.entity.persistent.Article;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository {
  List<Article> findArticles(
      int offset, int limit, List<String> tags, List<String> authors, List<String> favorited);

  Article create(Article article);

  boolean existsBySlug(String slug);

  Optional<Article> findBySlug(String slug);

  Article update(Article article);
}
