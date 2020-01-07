package org.example.realworldapi.domain.model.repository;

import org.example.realworldapi.domain.model.entity.persistent.ArticlesTags;
import org.example.realworldapi.domain.model.entity.persistent.Tag;

import java.util.List;

public interface ArticlesTagsRepository {
  List<Tag> findTags(Long articleId);

  ArticlesTags create(ArticlesTags articlesTags);
}
