package org.example.realworldapi.domain.feature.impl;

import lombok.AllArgsConstructor;
import org.example.realworldapi.domain.feature.CreateComment;
import org.example.realworldapi.domain.feature.FindArticleBySlug;
import org.example.realworldapi.domain.feature.FindUserById;
import org.example.realworldapi.domain.model.comment.Comment;
import org.example.realworldapi.domain.model.comment.CommentBuilder;
import org.example.realworldapi.domain.model.comment.NewCommentInput;
import org.example.realworldapi.domain.model.comment.NewCommentRepository;

@AllArgsConstructor
public class CreateCommentImpl implements CreateComment {

  private final NewCommentRepository commentRepository;
  private final FindUserById findUserById;
  private final FindArticleBySlug findArticleBySlug;
  private final CommentBuilder commentBuilder;

  @Override
  public Comment handle(NewCommentInput newCommentInput) {
    final var author = findUserById.handle(newCommentInput.getAuthorId());
    final var article = findArticleBySlug.handle(newCommentInput.getArticleSlug());
    final var comment = commentBuilder.build(author, article, newCommentInput.getBody());
    commentRepository.save(comment);
    return comment;
  }
}
