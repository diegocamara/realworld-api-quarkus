package org.example.realworldapi.domain.service;

import org.example.realworldapi.domain.model.entity.User;

public interface UsersService {
  User create(String username, String email, String password);

  User login(String email, String password);

  User findById(Long id);

  User update(User user);

  User findByUsername(String username);
}
