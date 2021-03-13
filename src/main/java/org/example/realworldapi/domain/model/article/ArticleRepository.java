package org.example.realworldapi.domain.model.article;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ArticleRepository {
  boolean existsBySlug(String slug);

  void save(Article article);

  Optional<Article> findArticleById(UUID id);

  Optional<Article> findBySlug(String slug);

  void update(Article article);

  Optional<Article> findByAuthorAndSlug(UUID authorId, String slug);

  void delete(Article article);

  PageResult<Article> findMostRecentArticlesByFilter(ArticleFilter articleFilter);

  PageResult<Article> findArticlesByFilter(ArticleFilter filter);

  long count(List<String> tags, List<String> authors, List<String> favorited);
}
