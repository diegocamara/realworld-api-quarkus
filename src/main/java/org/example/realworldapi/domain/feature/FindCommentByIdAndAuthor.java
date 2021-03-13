package org.example.realworldapi.domain.feature;

import org.example.realworldapi.domain.model.comment.Comment;

import java.util.UUID;

public interface FindCommentByIdAndAuthor {
  Comment handle(UUID commentId, UUID authorId);
}
