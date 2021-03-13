package org.example.realworldapi.domain.feature.impl;

import lombok.AllArgsConstructor;
import org.example.realworldapi.domain.feature.FindCommentByIdAndAuthor;
import org.example.realworldapi.domain.model.comment.Comment;
import org.example.realworldapi.domain.model.comment.NewCommentRepository;
import org.example.realworldapi.domain.model.exception.CommentNotFoundException;

import java.util.UUID;

@AllArgsConstructor
public class FindCommentByIdAndAuthorImpl implements FindCommentByIdAndAuthor {

  private final NewCommentRepository commentRepository;

  @Override
  public Comment handle(UUID commentId, UUID authorId) {
    return commentRepository
        .findByIdAndAuthor(commentId, authorId)
        .orElseThrow(CommentNotFoundException::new);
  }
}
