package org.example.realworldapi.domain.model.repository;

import org.example.realworldapi.domain.model.entity.User;

import java.util.Optional;

public interface UserRepository {
  User create(User user);

  Optional<User> findUserByEmail(String email);

  boolean existsBy(String field, String value);

  Optional<User> findUserById(Long id);

  boolean existsUsername(Long excludeId, String username);

  boolean existsEmail(Long excludeId, String email);

  Optional<User> findByUsernameOptional(String username);
}
