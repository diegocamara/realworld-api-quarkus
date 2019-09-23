package org.example.realworldapi.domain.resource.service.impl;

import org.example.realworldapi.domain.entity.persistent.User;
import org.example.realworldapi.domain.exception.*;
import org.example.realworldapi.domain.repository.UserRepository;
import org.example.realworldapi.domain.resource.service.UsersService;
import org.example.realworldapi.domain.security.Role;
import org.example.realworldapi.domain.service.JWTService;
import org.mindrot.jbcrypt.BCrypt;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.util.Optional;

@ApplicationScoped
public class UsersServiceImpl implements UsersService {

  private UserRepository userRepository;
  private JWTService jwtService;

  public UsersServiceImpl(UserRepository userRepository, JWTService jwtService) {
    this.userRepository = userRepository;
    this.jwtService = jwtService;
  }

  @Override
  @Transactional
  public User create(String username, String email, String password) {

    checkExistingUsername(username);
    checkExistingEmail(email);

    User user = new User();
    user.setUsername(username);
    user.setEmail(email.toUpperCase().trim());
    user.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));

    Optional<User> resultUser = userRepository.create(user);

    resultUser.ifPresent(createdUser -> createdUser.setToken(createJWT(createdUser)));

    return resultUser.orElseThrow(UserNotCreatedException::new);
  }

  private String createJWT(User user) {
    return jwtService.sign(user.getId().toString(), Role.USER);
  }

  private void checkExistingUsername(String username) {
    if (userRepository.existsBy("username", username)) {
      throw new UsernameAlreadyExistsException();
    }
  }

  private void checkExistingEmail(String email) {
    if (userRepository.existsBy("email", email)) {
      throw new EmailAlreadyExistsException();
    }
  }

  @Override
  @Transactional
  public User login(String email, String password) {

    Optional<User> userOptional = userRepository.findByEmail(email);

    if (!userOptional.isPresent()) {
      throw new UserNotFoundException();
    }

    if (isPasswordInvalid(password, userOptional.get())) {
      throw new InvalidPasswordException();
    }

    userOptional.ifPresent(loggedUser -> loggedUser.setToken(createJWT(loggedUser)));

    return userRepository.update(userOptional.get());
  }

  @Override
  @Transactional
  public User findById(Long id) {
    return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
  }

  @Override
  @Transactional
  public User update(User user) {

    checkValidations(user);

    Optional<User> managedUserOptional = userRepository.findById(user.getId());

    managedUserOptional.ifPresent(
        storedUser -> {
          if (isPresent(user.getUsername())) {
            storedUser.setUsername(user.getUsername());
          }

          if (isPresent(user.getEmail())) {
            storedUser.setEmail(user.getEmail());
          }

          if (isPresent(user.getBio())) {
            storedUser.setBio(user.getBio());
          }

          if (isPresent(user.getImage())) {
            storedUser.setImage(user.getImage());
          }
        });

    return managedUserOptional.orElse(null);
  }

  @Override
  @Transactional
  public User findByUsername(String username) {
    return userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
  }

  private boolean isPresent(String property) {
    return property != null && !property.isEmpty();
  }

  private boolean isPasswordInvalid(String password, User user) {
    return !BCrypt.checkpw(password, user.getPassword());
  }

  private void checkValidations(User user) {

    if (isPresent(user.getUsername())) {
      checkUsername(user.getId(), user.getUsername());
    }

    if (isPresent(user.getEmail())) {
      checkEmail(user.getId(), user.getEmail());
    }
  }

  private void checkUsername(Long selfId, String username) {
    if (userRepository.existsUsername(selfId, username)) {
      throw new UsernameAlreadyExistsException();
    }
  }

  private void checkEmail(Long selfId, String email) {
    if (userRepository.existsEmail(selfId, email)) {
      throw new EmailAlreadyExistsException();
    }
  }
}
