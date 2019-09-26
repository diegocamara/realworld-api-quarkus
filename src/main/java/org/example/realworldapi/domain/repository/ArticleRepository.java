package org.example.realworldapi.domain.repository;

import org.example.realworldapi.domain.entity.persistent.Article;

import java.util.List;

public interface ArticleRepository {
  List<Article> findArticles(
      int offset, int limit, List<String> tags, List<String> authors, List<String> favorited);
}
