package org.example.realworldapi.domain.service;

import org.example.realworldapi.domain.entity.User;

public interface UsersService {
  User create(String username, String email, String password);

  User login(String email, String password);

  User findById(Long id);

  User update(User user);

  User findByUsername(String username);
}
