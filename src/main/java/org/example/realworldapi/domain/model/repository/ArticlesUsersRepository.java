package org.example.realworldapi.domain.model.repository;

import org.example.realworldapi.domain.model.entity.ArticlesUsers;
import org.example.realworldapi.domain.model.entity.ArticlesUsersKey;

import java.util.Optional;

public interface ArticlesUsersRepository {
  boolean isFavorited(Long articleId, Long currentUserId);

  long favoritesCount(Long articleId);

  ArticlesUsers create(ArticlesUsers articlesUsers);

  Optional<ArticlesUsers> findById(ArticlesUsersKey articlesUsersKey);

  void remove(ArticlesUsers articlesUsers);
}
