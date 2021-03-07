package org.example.realworldapi.domain.feature.impl;

import lombok.AllArgsConstructor;
import org.example.realworldapi.domain.feature.FindUserByUsername;
import org.example.realworldapi.domain.model.exception.UserNotFoundException;
import org.example.realworldapi.domain.model.user.NewUserRepository;
import org.example.realworldapi.domain.model.user.User;

@AllArgsConstructor
public class FindUserByUsernameImpl implements FindUserByUsername {

  private final NewUserRepository userRepository;

  @Override
  public User handle(String username) {
    return userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
  }
}
