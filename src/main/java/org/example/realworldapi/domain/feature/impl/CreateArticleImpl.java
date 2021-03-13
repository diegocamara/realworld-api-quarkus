package org.example.realworldapi.domain.feature.impl;

import lombok.AllArgsConstructor;
import org.example.realworldapi.domain.feature.CreateArticle;
import org.example.realworldapi.domain.feature.FindTagsByNameCreateIfNotExists;
import org.example.realworldapi.domain.feature.FindUserById;
import org.example.realworldapi.domain.model.article.*;
import org.example.realworldapi.domain.model.provider.SlugProvider;
import org.example.realworldapi.domain.model.tag.Tag;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
public class CreateArticleImpl implements CreateArticle {

  private final FindUserById findUserById;
  private final NewArticleRepository articleRepository;
  private final ArticleModelBuilder articleBuilder;
  private final SlugProvider slugProvider;
  private final FindTagsByNameCreateIfNotExists findTagsByNameCreateIfNotExists;
  private final TagRelationshipRepository tagRelationshipRepository;

  @Override
  public Article handle(NewArticleInput newArticleInput) {
    final var author = findUserById.handle(newArticleInput.getAuthorId());
    final var slug = slug(newArticleInput.getTitle());
    final var article =
        articleBuilder.build(
            slug,
            newArticleInput.getTitle(),
            newArticleInput.getDescription(),
            newArticleInput.getBody(),
            author);
    articleRepository.save(article);
    final var tags = findTagsByNameCreateIfNotExists.handle(newArticleInput.getTagList());
    createTagRelationship(article, tags);
    return article;
  }

  private void createTagRelationship(Article article, List<Tag> tags) {
    tags.forEach(tag -> tagRelationshipRepository.save(new TagRelationship(article, tag)));
  }

  private String slug(String title) {
    String slug = slugProvider.slugify(title);
    if (articleRepository.existsBySlug(slug)) {
      slug += UUID.randomUUID().toString();
    }
    return slug;
  }
}
