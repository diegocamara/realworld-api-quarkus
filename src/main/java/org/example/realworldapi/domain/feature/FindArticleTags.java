package org.example.realworldapi.domain.feature;

import org.example.realworldapi.domain.model.article.Article;
import org.example.realworldapi.domain.model.tag.Tag;

import java.util.List;

public interface FindArticleTags {
  List<Tag> handle(Article article);
}
