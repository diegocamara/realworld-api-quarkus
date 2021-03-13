package org.example.realworldapi.domain.feature.impl;

import lombok.AllArgsConstructor;
import org.example.realworldapi.domain.exception.InvalidPasswordException;
import org.example.realworldapi.domain.exception.UserNotFoundException;
import org.example.realworldapi.domain.feature.LoginUser;
import org.example.realworldapi.domain.model.provider.HashProvider;
import org.example.realworldapi.domain.model.user.LoginUserInput;
import org.example.realworldapi.domain.model.user.User;
import org.example.realworldapi.domain.model.user.UserRepository;

@AllArgsConstructor
public class LoginUserImpl implements LoginUser {

  private final UserRepository userRepository;
  private final HashProvider hashProvider;

  @Override
  public User handle(LoginUserInput loginUserInput) {
    final var user =
        userRepository
            .findByEmail(loginUserInput.getEmail())
            .orElseThrow(UserNotFoundException::new);
    if (!isPasswordValid(loginUserInput.getPassword(), user.getPassword())) {
      throw new InvalidPasswordException();
    }
    return user;
  }

  private boolean isPasswordValid(String password, String hashedPassword) {
    return hashProvider.checkPassword(password, hashedPassword);
  }
}
