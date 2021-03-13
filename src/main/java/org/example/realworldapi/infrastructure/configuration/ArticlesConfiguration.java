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
      SlugProvider slugProvider,
      FindTagsByNameCreateIfNotExists findTagsByNameCreateIfNotExists,
      TagRelationshipRepository tagRelationshipRepository) {
    return new CreateArticleImpl(
        findUserById,
        articleRepository,
        articleBuilder,
        slugProvider,
        findTagsByNameCreateIfNotExists,
        tagRelationshipRepository);
  }

  @Produces
  @Singleton
  public FindArticleById findArticleById(NewArticleRepository articleRepository) {
    return new FindArticleByIdImpl(articleRepository);
  }

  @Produces
  @Singleton
  public FindArticleTags findArticleTags(TagRelationshipRepository tagRelationshipRepository) {
    return new FindArticleTagsImpl(tagRelationshipRepository);
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
  public ArticleModelBuilder articleBuilder(ModelValidator modelValidator) {
    return new ArticleModelBuilder(modelValidator);
  }
}
