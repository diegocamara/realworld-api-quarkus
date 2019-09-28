package org.example.realworldapi.domain.repository;

import org.example.realworldapi.domain.entity.persistent.ArticlesTags;
import org.example.realworldapi.domain.entity.persistent.Tag;

import java.util.List;

public interface ArticlesTagsRepository {
  List<Tag> findTags(Long articleId);

  ArticlesTags create(ArticlesTags articlesTags);
}
