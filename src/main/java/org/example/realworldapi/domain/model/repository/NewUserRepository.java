package org.example.realworldapi.domain.model.repository;

import org.example.realworldapi.domain.model.user.User;

import java.util.Optional;

public interface NewUserRepository {
  void save(User user);

  boolean existsBy(String field, String value);

  Optional<User> findUserByEmail(String email);
}
