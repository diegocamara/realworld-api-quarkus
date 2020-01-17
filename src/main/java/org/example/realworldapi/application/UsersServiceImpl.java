package org.example.realworldapi.application;

import org.example.realworldapi.domain.model.entity.User;
import org.example.realworldapi.domain.model.exception.EmailAlreadyExistsException;
import org.example.realworldapi.domain.model.exception.InvalidPasswordException;
import org.example.realworldapi.domain.model.exception.UserNotFoundException;
import org.example.realworldapi.domain.model.exception.UsernameAlreadyExistsException;
import org.example.realworldapi.domain.model.provider.HashProvider;
import org.example.realworldapi.domain.model.provider.TokenProvider;
import org.example.realworldapi.domain.model.repository.UserRepository;
import org.example.realworldapi.domain.service.UsersService;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.util.Optional;

@ApplicationScoped
public class UsersServiceImpl implements UsersService {

  private UserRepository userRepository;
  private TokenProvider tokenProvider;
  private HashProvider hashProvider;

  public UsersServiceImpl(
      UserRepository userRepository, TokenProvider tokenProvider, HashProvider hashProvider) {
    this.userRepository = userRepository;
    this.tokenProvider = tokenProvider;
    this.hashProvider = hashProvider;
  }

  @Override
  @Transactional
  public User create(String username, String email, String password) {

    checkExistingUsername(username);
    checkExistingEmail(email);

    User user = new User();
    user.setUsername(username);
    user.setEmail(email.toUpperCase().trim());
    user.setPassword(hashProvider.hashPassword(password));

    User resultUser = userRepository.create(user);
    resultUser.setToken(createToken(resultUser));

    return resultUser;
  }

  private String createToken(User user) {
    return tokenProvider.createUserToken(user.getId().toString());
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

    User user = userRepository.findUserByEmail(email).orElseThrow(UserNotFoundException::new);

    if (isPasswordInvalid(password, user.getPassword())) {
      throw new InvalidPasswordException();
    }

    user.setToken(createToken(user));

    return user;
  }

  @Override
  @Transactional
  public User findById(Long id) {
    return userRepository.findUserById(id).orElseThrow(UserNotFoundException::new);
  }

  @Override
  @Transactional
  public User update(User user) {

    checkValidations(user);

    Optional<User> managedUserOptional = userRepository.findUserById(user.getId());

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
    return userRepository.findByUsernameOptional(username).orElseThrow(UserNotFoundException::new);
  }

  private boolean isPresent(String property) {
    return property != null && !property.isEmpty();
  }

  private boolean isPasswordInvalid(String password, String hashedPassword) {
    return !hashProvider.checkPassword(password, hashedPassword);
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
