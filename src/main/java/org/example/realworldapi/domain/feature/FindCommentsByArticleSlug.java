package org.example.realworldapi.domain.feature;

import org.example.realworldapi.domain.model.comment.Comment;

import java.util.List;

public interface FindCommentsByArticleSlug {
  List<Comment> handle(String slug);
}
