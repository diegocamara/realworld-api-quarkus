package org.example.realworldapi.domain.feature;

import org.example.realworldapi.domain.model.article.Article;
import org.example.realworldapi.domain.model.article.UpdateArticleInput;

public interface UpdateArticleBySlug {
  Article handle(UpdateArticleInput updateArticleInput);
}
