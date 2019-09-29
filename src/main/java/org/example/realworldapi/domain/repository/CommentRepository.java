package org.example.realworldapi.domain.repository;

import org.example.realworldapi.domain.entity.persistent.Comment;

public interface CommentRepository {
  Comment create(Comment comment);

  Comment findComment(String slug, Long commentId, Long authorId);

  void delete(Comment comment);
}
