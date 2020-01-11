package org.example.realworldapi.domain.model.repository;

import org.example.realworldapi.domain.model.entity.ArticlesTags;
import org.example.realworldapi.domain.model.entity.Tag;

import java.util.List;

public interface ArticlesTagsRepository {
  List<Tag> findTags(Long articleId);

  ArticlesTags create(ArticlesTags articlesTags);
}
