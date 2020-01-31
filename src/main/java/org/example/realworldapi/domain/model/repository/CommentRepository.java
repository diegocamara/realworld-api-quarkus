package org.example.realworldapi.domain.model.repository;

import org.example.realworldapi.domain.model.entity.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {
  Comment create(Comment comment);

  Optional<Comment> findComment(String slug, Long commentId, Long authorId);

  void remove(Comment comment);

  List<Comment> findArticleComments(Long articleId);
}
