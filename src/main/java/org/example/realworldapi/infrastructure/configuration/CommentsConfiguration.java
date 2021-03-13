package org.example.realworldapi.infrastructure.configuration;

import org.example.realworldapi.domain.feature.CreateComment;
import org.example.realworldapi.domain.feature.FindArticleBySlug;
import org.example.realworldapi.domain.feature.FindUserById;
import org.example.realworldapi.domain.feature.impl.CreateCommentImpl;
import org.example.realworldapi.domain.model.comment.CommentBuilder;
import org.example.realworldapi.domain.model.comment.NewCommentRepository;
import org.example.realworldapi.domain.validator.ModelValidator;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

@Dependent
public class CommentsConfiguration {

  @Produces
  @Singleton
  public CreateComment createComment(
      NewCommentRepository commentRepository,
      FindUserById findUserById,
      FindArticleBySlug findArticleBySlug,
      CommentBuilder commentBuilder) {
    return new CreateCommentImpl(
        commentRepository, findUserById, findArticleBySlug, commentBuilder);
  }

  @Produces
  @Singleton
  public CommentBuilder commentBuilder(ModelValidator modelValidator) {
    return new CommentBuilder(modelValidator);
  }
}
