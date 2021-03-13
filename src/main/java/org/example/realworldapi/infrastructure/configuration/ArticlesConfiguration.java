package org.example.realworldapi.infrastructure.configuration;

import org.example.realworldapi.domain.feature.*;
import org.example.realworldapi.domain.feature.impl.*;
import org.example.realworldapi.domain.model.article.ArticleModelBuilder;
import org.example.realworldapi.domain.model.article.FavoriteRelationshipRepository;
import org.example.realworldapi.domain.model.article.NewArticleRepository;
import org.example.realworldapi.domain.model.article.TagRelationshipRepository;
import org.example.realworldapi.domain.model.provider.SlugProvider;
import org.example.realworldapi.domain.validator.ModelValidator;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

@Dependent
public class ArticlesConfiguration {

  @Produces
  @Singleton
  public CreateArticle createArticle(
      FindUserById findUserById,
      NewArticleRepository articleRepository,
      ArticleModelBuilder articleBuilder,
      CreateSlugByTitle createSlugByTitle,
      FindTagsByNameCreateIfNotExists findTagsByNameCreateIfNotExists,
      TagRelationshipRepository tagRelationshipRepository) {
    return new CreateArticleImpl(
        findUserById,
        articleRepository,
        articleBuilder,
        createSlugByTitle,
        findTagsByNameCreateIfNotExists,
        tagRelationshipRepository);
  }

  @Produces
  @Singleton
  public UpdateArticleBySlug updateArticleBySlug(
      FindArticleBySlug findArticleBySlug,
      CreateSlugByTitle createSlugByTitle,
      NewArticleRepository articleRepository,
      ModelValidator modelValidator) {
    return new UpdateArticleBySlugImpl(
        findArticleBySlug, createSlugByTitle, articleRepository, modelValidator);
  }

  @Produces
  @Singleton
  public DeleteArticleBySlug deleteArticleBySlug(
      FindArticleByAuthorAndSlug findArticleByAuthorAndSlug,
      NewArticleRepository articleRepository) {
    return new DeleteArticleBySlugImpl(findArticleByAuthorAndSlug, articleRepository);
  }

  @Produces
  @Singleton
  public FindArticleById findArticleById(NewArticleRepository articleRepository) {
    return new FindArticleByIdImpl(articleRepository);
  }

  @Produces
  @Singleton
  public FindArticleByAuthorAndSlug findArticleByAuthorAndSlug(
      NewArticleRepository articleRepository) {
    return new FindArticleByAuthorAndSlugImpl(articleRepository);
  }

  @Produces
  @Singleton
  public FindArticleBySlug findArticleBySlug(NewArticleRepository articleRepository) {
    return new FindArticleBySlugImpl(articleRepository);
  }

  @Produces
  @Singleton
  public FindArticleTags findArticleTags(TagRelationshipRepository tagRelationshipRepository) {
    return new FindArticleTagsImpl(tagRelationshipRepository);
  }

  @Produces
  @Singleton
  public FindMostRecentArticlesByFilter findMostRecentArticlesByFilter(
      NewArticleRepository articleRepository) {
    return new FindMostRecentArticlesByFilterImpl(articleRepository);
  }

  @Produces
  @Singleton
  public FindArticlesByFilter findArticlesByFilter(NewArticleRepository articleRepository) {
    return new FindArticlesByFilterImpl(articleRepository);
  }

  @Produces
  @Singleton
  public IsArticleFavorited isArticleFavorited(
      FavoriteRelationshipRepository favoriteRelationshipRepository) {
    return new IsArticleFavoritedImpl(favoriteRelationshipRepository);
  }

  @Produces
  @Singleton
  public ArticleFavoritesCount articleFavoritesCount(
      FindArticleById findArticleById,
      FavoriteRelationshipRepository favoriteRelationshipRepository) {
    return new ArticleFavoritesCountImpl(findArticleById, favoriteRelationshipRepository);
  }

  @Produces
  @Singleton
  public CreateSlugByTitle createSlugByTitle(
      NewArticleRepository articleRepository, SlugProvider slugProvider) {
    return new CreateSlugByTitleImpl(articleRepository, slugProvider);
  }

  @Produces
  @Singleton
  public FavoriteArticle favoriteArticle(
      FindArticleBySlug findArticleBySlug,
      FindUserById findUserById,
      FavoriteRelationshipRepository favoriteRelationshipRepository) {
    return new FavoriteArticleImpl(findArticleBySlug, findUserById, favoriteRelationshipRepository);
  }

  @Produces
  @Singleton
  public UnfavoriteArticle unfavoriteArticle(
      FindArticleBySlug findArticleBySlug,
      FavoriteRelationshipRepository favoriteRelationshipRepository) {
    return new UnfavoriteArticleImpl(findArticleBySlug, favoriteRelationshipRepository);
  }

  @Produces
  @Singleton
  public ArticleModelBuilder articleBuilder(ModelValidator modelValidator) {
    return new ArticleModelBuilder(modelValidator);
  }
}
