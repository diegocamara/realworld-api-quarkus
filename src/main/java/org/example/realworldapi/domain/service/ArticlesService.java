package org.example.realworldapi.domain.service;

import org.example.realworldapi.application.data.ArticleData;
import org.example.realworldapi.application.data.ArticlesData;
import org.example.realworldapi.application.data.CommentData;

import java.util.List;

public interface ArticlesService {
  ArticlesData findRecentArticles(Long loggedUserId, int offset, int limit);

  ArticlesData findArticles(
      int offset,
      int limit,
      Long loggedUserId,
      List<String> tags,
      List<String> authors,
      List<String> favorited);

  ArticleData create(
      String title, String description, String body, List<String> tagList, Long authorId);

  ArticleData findBySlug(String slug);

  ArticleData update(String slug, String title, String description, String body, Long authorId);

  void delete(String slug, Long authorId);

  List<CommentData> findCommentsBySlug(String slug, Long loggedUserId);

  CommentData createComment(String slug, String body, Long commentAuthorId);

  void deleteComment(String slug, Long commentId, Long loggedUserId);

  ArticleData favoriteArticle(String slug, Long loggedUserId);

  ArticleData unfavoriteArticle(String slug, Long loggedUserId);
}
