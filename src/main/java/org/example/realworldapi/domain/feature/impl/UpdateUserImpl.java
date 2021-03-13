package org.example.realworldapi.domain.feature.impl;

import lombok.AllArgsConstructor;
import org.example.realworldapi.domain.feature.FindUserById;
import org.example.realworldapi.domain.feature.UpdateUser;
import org.example.realworldapi.domain.exception.EmailAlreadyExistsException;
import org.example.realworldapi.domain.exception.UsernameAlreadyExistsException;
import org.example.realworldapi.domain.model.user.UpdateUserInput;
import org.example.realworldapi.domain.model.user.User;
import org.example.realworldapi.domain.model.user.UserRepository;
import org.example.realworldapi.domain.validator.ModelValidator;

import java.util.UUID;

@AllArgsConstructor
public class UpdateUserImpl implements UpdateUser {

  private final FindUserById findUserById;
  private final UserRepository userRepository;
  private final ModelValidator modelValidator;

  @Override
  public User handle(UpdateUserInput updateUserInput) {
    final var user = findUserById.handle(updateUserInput.getId());
    checkValidations(updateUserInput, updateUserInput.getId());
    updateFields(user, updateUserInput);
    userRepository.update(modelValidator.validate(user));
    return user;
  }

  private void updateFields(User user, UpdateUserInput updateUserInput) {
    if (isPresent(updateUserInput.getUsername())) {
      user.setUsername(updateUserInput.getUsername());
    }

    if (isPresent(updateUserInput.getEmail())) {
      user.setEmail(updateUserInput.getEmail());
    }

    if (isPresent(updateUserInput.getBio())) {
      user.setBio(updateUserInput.getBio());
    }

    if (isPresent(updateUserInput.getImage())) {
      user.setImage(updateUserInput.getImage());
    }
  }

  private void checkValidations(UpdateUserInput updateUserInput, UUID excludeId) {

    if (isPresent(updateUserInput.getUsername())) {
      checkUsername(excludeId, updateUserInput.getUsername());
    }

    if (isPresent(updateUserInput.getEmail())) {
      checkEmail(excludeId, updateUserInput.getEmail());
    }
  }

  private boolean isPresent(String property) {
    return property != null && !property.isEmpty();
  }

  private void checkUsername(UUID selfId, String username) {
    if (userRepository.existsUsername(selfId, username)) {
      throw new UsernameAlreadyExistsException();
    }
  }

  private void checkEmail(UUID selfId, String email) {
    if (userRepository.existsEmail(selfId, email)) {
      throw new EmailAlreadyExistsException();
    }
  }
}
