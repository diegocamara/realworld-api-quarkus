package org.example.realworldapi.domain.repository;

import org.example.realworldapi.domain.entity.persistent.Comment;

import java.util.Optional;

public interface CommentRepository {
  Comment create(Comment comment);

  Optional<Comment> findComment(String slug, Long commentId, Long authorId);

  void delete(Comment comment);
}
