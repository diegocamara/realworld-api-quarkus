package org.example.realworldapi.domain.feature;

import org.example.realworldapi.domain.model.comment.DeleteCommentInput;

public interface DeleteComment {
  void handle(DeleteCommentInput deleteCommentInput);
}
