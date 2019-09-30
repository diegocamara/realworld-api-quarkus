package org.example.realworldapi.domain.repository;

import org.example.realworldapi.domain.entity.persistent.ArticlesUsers;
import org.example.realworldapi.domain.entity.persistent.ArticlesUsersKey;

import java.util.Optional;

public interface ArticlesUsersRepository {
  boolean isFavorited(Long articleId, Long currentUserId);

  int favoritesCount(Long articleId);

  ArticlesUsers create(ArticlesUsers articlesUsers);

  Optional<ArticlesUsers> findById(ArticlesUsersKey articlesUsersKey);

  void remove(ArticlesUsers articlesUsers);
}
