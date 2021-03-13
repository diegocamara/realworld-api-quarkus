package org.example.realworldapi.domain.feature.impl;

import lombok.AllArgsConstructor;
import org.example.realworldapi.domain.feature.FindUserByUsername;
import org.example.realworldapi.domain.exception.UserNotFoundException;
import org.example.realworldapi.domain.model.user.User;
import org.example.realworldapi.domain.model.user.UserRepository;

@AllArgsConstructor
public class FindUserByUsernameImpl implements FindUserByUsername {

  private final UserRepository userRepository;

  @Override
  public User handle(String username) {
    return userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
  }
}
