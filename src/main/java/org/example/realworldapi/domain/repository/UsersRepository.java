package org.example.realworldapi.domain.repository;

import org.example.realworldapi.domain.entity.User;

import java.util.Optional;

public interface UsersRepository {
    User create(User user);
    Optional<User> findByEmail(String email);
    boolean exists(String email);
}
