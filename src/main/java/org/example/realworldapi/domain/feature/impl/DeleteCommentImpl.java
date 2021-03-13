package org.example.realworldapi.domain.feature.impl;

import lombok.AllArgsConstructor;
import org.example.realworldapi.domain.feature.DeleteComment;
import org.example.realworldapi.domain.feature.FindCommentByIdAndAuthor;
import org.example.realworldapi.domain.model.comment.CommentRepository;
import org.example.realworldapi.domain.model.comment.DeleteCommentInput;

@AllArgsConstructor
public class DeleteCommentImpl implements DeleteComment {

  private final FindCommentByIdAndAuthor findCommentByIdAndAuthor;
  private final CommentRepository commentRepository;

  @Override
  public void handle(DeleteCommentInput deleteCommentInput) {
    final var comment =
        findCommentByIdAndAuthor.handle(
            deleteCommentInput.getCommentId(), deleteCommentInput.getAuthorId());
    commentRepository.delete(comment);
  }
}
