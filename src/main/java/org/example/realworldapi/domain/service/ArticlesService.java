package org.example.realworldapi.domain.service;

import org.example.realworldapi.domain.model.entity.Article;
import org.example.realworldapi.domain.model.entity.Articles;
import org.example.realworldapi.domain.model.entity.Comment;

import java.util.List;

public interface ArticlesService {
  Articles findRecentArticles(Long loggedUserId, int offset, int limit);

  Articles findArticles(
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

  void delete(String slug, Long authorId);

  List<Comment> findCommentsBySlug(String slug, Long loggedUserId);

  Comment createComment(String slug, String body, Long commentAuthorId);

  void deleteComment(String slug, Long commentId, Long loggedUserId);

  Article favoriteArticle(String slug, Long loggedUserId);

  Article unfavoriteArticle(String slug, Long loggedUserId);
}
