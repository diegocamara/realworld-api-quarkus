package org.example.realworldapi.domain.resource.service;

import org.example.realworldapi.domain.entity.persistent.Article;

import java.util.List;

public interface ArticlesService {
  List<Article> findRecentArticles(Long loggedUserId, int offset, int limit);
}
