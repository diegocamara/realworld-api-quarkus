package org.example.realworldapi.domain.model.repository;

import org.example.realworldapi.domain.model.user.User;

import java.util.Optional;
import java.util.UUID;

public interface NewUserRepository {
  void save(User user);

  boolean existsBy(String field, String value);

  Optional<User> findByEmail(String email);

  Optional<User> findUserById(UUID id);

  boolean existsUsername(UUID excludeId, String username);

  boolean existsEmail(UUID excludeId, String email);

  void update(User user);
}
