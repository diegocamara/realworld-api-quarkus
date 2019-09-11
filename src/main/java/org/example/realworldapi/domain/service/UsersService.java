package org.example.realworldapi.domain.service;

import org.example.realworldapi.domain.entity.User;

import java.util.Optional;

public interface UsersService {
    User create(String username, String email, String password);
    Optional<User> login(String email, String password);
    Optional<User> findById(Long id);
}
