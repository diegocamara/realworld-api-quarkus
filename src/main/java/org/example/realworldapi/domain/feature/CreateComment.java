package org.example.realworldapi.domain.feature;

import org.example.realworldapi.domain.model.comment.Comment;
import org.example.realworldapi.domain.model.comment.NewCommentInput;

public interface CreateComment {
  Comment handle(NewCommentInput newCommentInput);
}
