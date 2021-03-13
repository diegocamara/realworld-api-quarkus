package org.example.realworldapi.domain.feature.impl;

import lombok.AllArgsConstructor;
import org.example.realworldapi.domain.feature.FindUserById;
import org.example.realworldapi.domain.exception.UserNotFoundException;
import org.example.realworldapi.domain.model.user.User;
import org.example.realworldapi.domain.model.user.UserRepository;

import java.util.UUID;

@AllArgsConstructor
public class FindUserByIdImpl implements FindUserById {

  private final UserRepository userRepository;

  @Override
  public User handle(UUID id) {
    return userRepository.findUserById(id).orElseThrow(UserNotFoundException::new);
  }
}
