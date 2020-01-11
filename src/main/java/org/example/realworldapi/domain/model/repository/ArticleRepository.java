package org.example.realworldapi.domain.model.repository;

import org.example.realworldapi.domain.model.entity.Article;
import org.example.realworldapi.domain.model.entity.Comment;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository {
  List<Article> findArticles(
      int offset, int limit, List<String> tags, List<String> authors, List<String> favorited);

  Article create(Article article);

  boolean existsBySlug(String slug);

  Optional<Article> findBySlug(String slug);

  Article update(Article article);

  void delete(Article article);

  Optional<Article> findByIdAndSlug(Long authorId, String slug);

  List<Comment> findComments(Long articleId);

  int count(List<String> tags, List<String> authors, List<String> favorited);
}
