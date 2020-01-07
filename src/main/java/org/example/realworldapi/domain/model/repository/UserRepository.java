package org.example.realworldapi.domain.model.repository;

import org.example.realworldapi.domain.model.entity.persistent.User;

import java.util.Optional;

public interface UserRepository {
  User create(User user);

  Optional<User> findByEmail(String email);

  boolean existsBy(String field, String value);

  Optional<User> findById(Long id);

  User update(User user);

  boolean existsUsername(Long excludeId, String username);

  boolean existsEmail(Long excludeId, String email);

  Optional<User> findByUsername(String username);
}
