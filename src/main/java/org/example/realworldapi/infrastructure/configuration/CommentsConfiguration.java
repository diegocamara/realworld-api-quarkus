package org.example.realworldapi.infrastructure.configuration;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Singleton;
import org.example.realworldapi.domain.feature.*;
import org.example.realworldapi.domain.feature.impl.CreateCommentImpl;
import org.example.realworldapi.domain.feature.impl.DeleteCommentImpl;
import org.example.realworldapi.domain.feature.impl.FindCommentByIdAndAuthorImpl;
import org.example.realworldapi.domain.feature.impl.FindCommentsByArticleSlugImpl;
import org.example.realworldapi.domain.model.comment.CommentBuilder;
import org.example.realworldapi.domain.model.comment.CommentRepository;
import org.example.realworldapi.domain.validator.ModelValidator;

@Dependent
public class CommentsConfiguration {

  @Produces
  @Singleton
  public CreateComment createComment(
      CommentRepository commentRepository,
      FindUserById findUserById,
      FindArticleBySlug findArticleBySlug,
      CommentBuilder commentBuilder) {
    return new CreateCommentImpl(
        commentRepository, findUserById, findArticleBySlug, commentBuilder);
  }

  @Produces
  @Singleton
  public DeleteComment deleteComment(
      FindCommentByIdAndAuthor findCommentByIdAndAuthor, CommentRepository commentRepository) {
    return new DeleteCommentImpl(findCommentByIdAndAuthor, commentRepository);
  }

  @Produces
  @Singleton
  public FindCommentByIdAndAuthor findCommentByIdAndAuthor(CommentRepository commentRepository) {
    return new FindCommentByIdAndAuthorImpl(commentRepository);
  }

  @Produces
  @Singleton
  public FindCommentsByArticleSlug findCommentsByArticleSlug(
      FindArticleBySlug findArticleBySlug, CommentRepository commentRepository) {
    return new FindCommentsByArticleSlugImpl(findArticleBySlug, commentRepository);
  }

  @Produces
  @Singleton
  public CommentBuilder commentBuilder(ModelValidator modelValidator) {
    return new CommentBuilder(modelValidator);
  }
}
