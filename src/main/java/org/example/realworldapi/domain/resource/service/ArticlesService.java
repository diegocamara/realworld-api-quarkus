package org.example.realworldapi.domain.resource.service;

import org.example.realworldapi.domain.entity.Article;

import java.util.List;

public interface ArticlesService {
  List<Article> findRecentArticles(Long loggedUserId, int offset, int limit);

  List<Article> findArticles(
      int offset,
      int limit,
      Long loggedUserId,
      List<String> tags,
      List<String> authors,
      List<String> favorited);

  Article create(
      String title, String description, String body, List<String> tagList, Long authorId);

  Article findBySlug(String slug);

  Article update(String slug, String title, String description, String body, Long authorId);
}
