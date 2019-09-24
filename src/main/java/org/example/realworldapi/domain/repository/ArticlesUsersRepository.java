package org.example.realworldapi.domain.repository;

public interface ArticlesUsersRepository {
  boolean isFavorited(Long articleId, Long currentUserId);

  int favoritesCount(Long articleId);
}
