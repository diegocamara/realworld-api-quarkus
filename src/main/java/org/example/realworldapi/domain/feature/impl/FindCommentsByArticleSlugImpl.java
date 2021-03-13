package org.example.realworldapi.domain.feature.impl;

import lombok.AllArgsConstructor;
import org.example.realworldapi.domain.feature.FindArticleBySlug;
import org.example.realworldapi.domain.feature.FindCommentsByArticleSlug;
import org.example.realworldapi.domain.model.comment.Comment;
import org.example.realworldapi.domain.model.comment.CommentRepository;

import java.util.List;

@AllArgsConstructor
public class FindCommentsByArticleSlugImpl implements FindCommentsByArticleSlug {

  private final FindArticleBySlug findArticleBySlug;
  private final CommentRepository commentRepository;

  @Override
  public List<Comment> handle(String slug) {
    final var article = findArticleBySlug.handle(slug);
    return commentRepository.findCommentsByArticle(article);
  }
}
