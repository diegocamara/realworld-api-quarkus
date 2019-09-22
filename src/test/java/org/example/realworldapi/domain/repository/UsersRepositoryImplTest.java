package org.example.realworldapi.domain.repository;

import org.example.realworldapi.DatabaseIntegrationTest;
import org.example.realworldapi.domain.entity.User;
import org.example.realworldapi.domain.builder.UserBuilder;
import org.example.realworldapi.domain.repository.impl.UsersRepositoryImpl;
import org.example.realworldapi.domain.security.Role;
import org.example.realworldapi.util.UserUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

public class UsersRepositoryImplTest extends DatabaseIntegrationTest {

  private UsersRepository usersRepository;

  @BeforeEach
  public void beforeEach() {
    usersRepository = new UsersRepositoryImpl(entityManager);
  }

  @AfterEach
  public void afterEach() {
    clear();
  }

  @Test
  public void shouldCreateAnUser() {

    User user = UserUtils.create("user", "user@mail.com", "123");

    Optional<User> result = transaction(() -> usersRepository.create(user));

    transaction(
        () ->
            result.ifPresent(
                createdUser ->
                    Assertions.assertNotNull(entityManager.find(User.class, createdUser.getId()))));
  }

  @Test
  public void shouldReturnAUserByEmail() {

    User existingUser = createUser("user1", "user@mail.com", "123");

    Optional<User> result = transaction(() -> usersRepository.findByEmail(existingUser.getEmail()));

    Assertions.assertEquals(existingUser.getEmail(), result.orElse(new User()).getEmail());
  }

  @Test
  public void givenAExistingEmail_shouldReturnTrue() {

    User existingUser = createUser("user1", "user@mail.com", "123");

    transaction(
        () -> Assertions.assertTrue(usersRepository.existsBy("email", existingUser.getEmail())));
  }

  @Test
  public void givenAExistingUsername_shouldReturnTrue() {

    User existingUser = createUser("user1", "user@mail.com", "123");

    transaction(
        () ->
            Assertions.assertTrue(
                usersRepository.existsBy("username", existingUser.getUsername())));
  }

  @Test
  public void givenAInexistentEmail_shouldReturnFalse() {

    String email = "user@mail.com";

    transaction(() -> Assertions.assertFalse(usersRepository.existsBy("email", email)));
  }

  @Test
  public void givenAInexistentUsername_shouldReturnFalse() {

    String username = "user1";

    transaction(() -> Assertions.assertFalse(usersRepository.existsBy("username", username)));
  }

  @Test
  public void shouldReturnAnUpdatedUser() {

    User existingUser = createUser("user1", "user@mail.com", "123");

    User user = new UserBuilder().id(existingUser.getId()).username("user2").build();

    User result = transaction(() -> usersRepository.update(user));

    Assertions.assertEquals(user.getUsername(), result.getUsername());
  }

  @Test
  public void givenAnotherExistingUsername_shouldReturnTrue() {

    User otherUser = createUser("user1", "user@mail.com", "123");

    User currentUser = createUser("currentUser", "currentUser@mail.com", "123");

    transaction(
        () ->
            Assertions.assertTrue(
                usersRepository.existsUsername(currentUser.getId(), otherUser.getUsername())));
  }

  @Test
  public void givenInexistentUsername_shouldReturnFalse() {

    User currentUser = createUser("currentUser", "currentUser@mail.com", "123");

    transaction(
        () ->
            Assertions.assertFalse(
                usersRepository.existsUsername(currentUser.getId(), "superusername")));
  }

  @Test
  public void shouldReturnFalseWhenUseCurrentUserUsername() {

    User currentUser = createUser("currentUser", "currentUser@mail.com", "123");

    transaction(
        () ->
            Assertions.assertFalse(
                usersRepository.existsUsername(currentUser.getId(), currentUser.getUsername())));
  }

  @Test
  public void givenAnotherExistingEmail_shouldReturnTrue() {

    User otherUser = createUser("user1", "user@mail.com", "123");

    User currentUser = createUser("currentUser", "currentUser@mail.com", "123");

    transaction(
        () ->
            Assertions.assertTrue(
                usersRepository.existsEmail(currentUser.getId(), otherUser.getEmail())));
  }

  @Test
  public void givenInexistentEmail_shouldReturnFalse() {

    User currentUser = createUser("currentUser", "currentUser@mail.com", "123");

    transaction(
        () ->
            Assertions.assertFalse(
                usersRepository.existsEmail(currentUser.getId(), "user@mail.com")));
  }

  @Test
  public void shouldReturnFalseWhenUseCurrentEmail() {

    User currentUser = createUser("currentUser", "currentUser@mail.com", "123");

    transaction(
        () ->
            Assertions.assertFalse(
                usersRepository.existsEmail(currentUser.getId(), currentUser.getEmail())));
  }

  @Test
  public void givenValidUsername_shouldReturnUser() {

    User user = createUser("user", "user@mail.com", "123");

    transaction(
        () ->
            Assertions.assertTrue(usersRepository.findByUsername(user.getUsername()).isPresent()));
  }

  private User createUser(String username, String email, String password, Role... role) {
    return transaction(
        () -> {
          User user = UserUtils.create(username, email, password);
          entityManager.persist(user);
          return user;
        });
  }
}
