package org.example.realworldapi.domain.feature;

import org.example.realworldapi.domain.model.article.Article;
import org.example.realworldapi.domain.model.article.NewArticleInput;

public interface CreateArticle {
  Article handle(NewArticleInput newArticleInput);
}
