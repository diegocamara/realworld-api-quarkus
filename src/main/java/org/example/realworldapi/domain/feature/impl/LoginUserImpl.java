package org.example.realworldapi.domain.feature.impl;

import lombok.AllArgsConstructor;
import org.example.realworldapi.domain.feature.LoginUser;
import org.example.realworldapi.domain.model.exception.InvalidPasswordException;
import org.example.realworldapi.domain.model.exception.UserNotFoundException;
import org.example.realworldapi.domain.model.provider.HashProvider;
import org.example.realworldapi.domain.model.repository.NewUserRepository;
import org.example.realworldapi.domain.model.user.LoginUserInput;
import org.example.realworldapi.domain.model.user.User;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@AllArgsConstructor
public class LoginUserImpl implements LoginUser {

  private final NewUserRepository userRepository;
  private final HashProvider hashProvider;

  @Override
  public User handle(LoginUserInput loginUserInput) {
    final var user =
        userRepository
            .findUserByEmail(loginUserInput.getEmail())
            .orElseThrow(UserNotFoundException::new);
    if (isPasswordInvalid(loginUserInput.getPassword(), user.getPassword())) {
      throw new InvalidPasswordException();
    }
    return user;
  }

  private boolean isPasswordInvalid(String password, String hashedPassword) {
    return !hashProvider.checkPassword(password, hashedPassword);
  }
}
