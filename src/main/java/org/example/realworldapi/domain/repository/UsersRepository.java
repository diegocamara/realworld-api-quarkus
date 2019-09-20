package org.example.realworldapi.domain.repository;

import org.example.realworldapi.domain.entity.User;

import java.util.Optional;

public interface UsersRepository {
    Optional<User> create(User user);
    Optional<User> findByEmail(String email);
    boolean existsBy(String field, String value);
    Optional<User> findById(Long id);
    User update(User user);
    boolean existsUsername(Long excludeId, String username);
    boolean existsEmail(Long excludeId, String email);
}
