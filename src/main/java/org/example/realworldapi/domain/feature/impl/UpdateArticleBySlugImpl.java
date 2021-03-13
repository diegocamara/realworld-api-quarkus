package org.example.realworldapi.domain.feature.impl;

import lombok.AllArgsConstructor;
import org.example.realworldapi.domain.feature.CreateSlugByTitle;
import org.example.realworldapi.domain.feature.FindArticleBySlug;
import org.example.realworldapi.domain.feature.UpdateArticleBySlug;
import org.example.realworldapi.domain.model.article.Article;
import org.example.realworldapi.domain.model.article.ArticleRepository;
import org.example.realworldapi.domain.model.article.UpdateArticleInput;
import org.example.realworldapi.domain.validator.ModelValidator;

import java.time.LocalDateTime;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@AllArgsConstructor
public class UpdateArticleBySlugImpl implements UpdateArticleBySlug {

  private final FindArticleBySlug findArticleBySlug;
  private final CreateSlugByTitle createSlugByTitle;
  private final ArticleRepository articleRepository;
  private final ModelValidator modelValidator;

  @Override
  public Article handle(UpdateArticleInput updateArticleInput) {
    final var article = findArticleBySlug.handle(updateArticleInput.getSlug());
    if (atLeastOneFieldIsNotBlank(updateArticleInput)) {
      if (isNotBlank(updateArticleInput.getTitle())) {
        article.setSlug(createSlugByTitle.handle(updateArticleInput.getTitle()));
        article.setTitle(updateArticleInput.getTitle());
      }
      if (isNotBlank(updateArticleInput.getDescription())) {
        article.setDescription(updateArticleInput.getDescription());
      }
      if (isNotBlank(updateArticleInput.getBody())) {
        article.setBody(updateArticleInput.getBody());
      }
      article.setUpdatedAt(LocalDateTime.now());
      articleRepository.update(modelValidator.validate(article));
    }
    return article;
  }

  private boolean atLeastOneFieldIsNotBlank(UpdateArticleInput updateArticleInput) {
    return isNotBlank(updateArticleInput.getTitle())
        || isNotBlank(updateArticleInput.getDescription())
        || isNotBlank(updateArticleInput.getBody());
  }
}
