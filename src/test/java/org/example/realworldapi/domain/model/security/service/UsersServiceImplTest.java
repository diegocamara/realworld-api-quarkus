package org.example.realworldapi.domain.model.security.service;

import io.quarkus.test.junit.QuarkusTest;
import org.example.realworldapi.application.UsersServiceImpl;
import org.example.realworldapi.domain.model.builder.UserBuilder;
import org.example.realworldapi.domain.model.entity.User;
import org.example.realworldapi.domain.model.exception.EmailAlreadyExistsException;
import org.example.realworldapi.domain.model.exception.InvalidPasswordException;
import org.example.realworldapi.domain.model.exception.UserNotFoundException;
import org.example.realworldapi.domain.model.exception.UsernameAlreadyExistsException;
import org.example.realworldapi.domain.model.provider.HashProvider;
import org.example.realworldapi.domain.model.provider.TokenProvider;
import org.example.realworldapi.domain.model.repository.UserRepository;
import org.example.realworldapi.domain.service.UsersService;
import org.example.realworldapi.util.UserUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@QuarkusTest
public class UsersServiceImplTest {

  private UserRepository userRepository;
  private TokenProvider tokenProvider;
  private HashProvider hashProvider;
  private UsersService usersService;

  @BeforeEach
  public void beforeEach() {
    userRepository = mock(UserRepository.class);
    tokenProvider = mock(TokenProvider.class);
    hashProvider = mock(HashProvider.class);
    usersService = new UsersServiceImpl(userRepository, tokenProvider, hashProvider);
  }

  @Test
  public void givenValidNewUserData_thenReturnAnCreatedUserWithFilledTokenField() {

    String username = "user";
    String email = "user@email.com";
    String password = "user123";

    User createdUser = new User();
    createdUser.setId(1L);
    createdUser.setUsername(username);
    createdUser.setEmail(email);
    createdUser.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
    createdUser.setToken(UUID.randomUUID().toString());

    when(userRepository.create(any(User.class))).thenReturn(createdUser);
    when(tokenProvider.createUserToken(createdUser.getId().toString())).thenReturn("token");

    User resultUser = usersService.create(username, email, password);

    Assertions.assertNotNull(resultUser.getUsername());
    Assertions.assertNotNull(resultUser.getEmail());
    Assertions.assertNotNull(resultUser.getPassword());
    Assertions.assertNotNull(resultUser.getToken());
  }

  @Test
  public void whenExecuteCreateWithExistingEmail_shouldThrowsEmailAlreadyExistsException() {

    String username = "user";
    String email = "user@email.com";
    String password = "user123";

    when(userRepository.existsBy("email", email)).thenReturn(true);

    Assertions.assertThrows(
        EmailAlreadyExistsException.class, () -> usersService.create(username, email, password));
  }

  @Test
  public void whenExecuteCreateWithExistingUsername_shouldThrowsUsernameAlreadyExistsException() {

    String username = "user";
    String email = "user@email.com";
    String password = "user123";

    when(userRepository.existsBy("username", username)).thenReturn(true);

    Assertions.assertThrows(
        UsernameAlreadyExistsException.class, () -> usersService.create(username, email, password));
  }

  @Test
  public void givenAnValidLoginInfo_thenReturnsAUser() {

    String email = "user1@mail.com";
    String password = "123";

    Optional<User> existingUser = Optional.of(UserUtils.create(1L, "user1", email, password));

    when(userRepository.findUserByEmail(email)).thenReturn(existingUser);
    when(hashProvider.checkPassword(password, existingUser.get().getPassword())).thenReturn(true);
    when(tokenProvider.createUserToken(existingUser.get().getId().toString())).thenReturn("token");

    User resultUser = usersService.login(email, password);

    Assertions.assertEquals(existingUser.get(), resultUser);
  }

  @Test
  public void givenAInvalidEmail_thenUserNotFoundException() {

    String email = "user1@mail.com";
    String password = "123";

    when(userRepository.findUserByEmail(email)).thenReturn(Optional.empty());

    Assertions.assertThrows(UserNotFoundException.class, () -> usersService.login(email, password));
  }

  @Test
  public void givenAValidEmailAndAInvalidPassword_thenThrowsInvalidPasswordException() {

    String email = "user1@mail.com";
    String password = "123";

    Optional<User> existingUser = Optional.of(UserUtils.create("user1", email, password));

    when(userRepository.findUserByEmail(email)).thenReturn(existingUser);

    Assertions.assertThrows(InvalidPasswordException.class, () -> usersService.login(email, "158"));
  }

  @Test
  public void givenAPersistedUser_whenExecuteFindById_shouldRetrieveUser() {

    User user = UserUtils.create("User1", "user1@mail.com", "user123");
    user.setId(1L);

    Optional<User> userResponse = Optional.of(user);

    when(userRepository.findUserById(user.getId())).thenReturn(userResponse);

    User result = usersService.findById(user.getId());

    Assertions.assertEquals(user.getId(), result.getId());
  }

  @Test
  public void givenANotPersistedUser_whenExecuteFindById_shouldThrowsUseNotFoundException() {

    Long userId = 1L;

    when(userRepository.findUserById(userId)).thenReturn(Optional.empty());

    Assertions.assertThrows(UserNotFoundException.class, () -> usersService.findById(userId));
  }

  @Test
  public void givenAExistentUser_whenExecuteUpdate_shouldReturnUpdatedUser() {

    User user =
        new UserBuilder().id(1L).username("user1").bio("user1 bio").email("user1@mail.com").build();

    when(userRepository.findUserById(user.getId())).thenReturn(Optional.of(user));

    User result = usersService.update(user);

    Assertions.assertEquals(user.getEmail(), result.getEmail());
  }

  @Test
  public void givenAExistingUsername_shouldThrowsUsernameAlreadyExistsException() {

    User user =
        new UserBuilder().id(1L).username("user1").bio("user1 bio").email("user1@mail.com").build();

    when(userRepository.existsUsername(user.getId(), user.getUsername())).thenReturn(true);

    Assertions.assertThrows(UsernameAlreadyExistsException.class, () -> usersService.update(user));
  }

  @Test
  public void givenAExistingEmail_shouldThrowsEmailAlreadyExistsException() {

    User user =
        new UserBuilder().id(1L).username("user1").bio("user1 bio").email("user1@mail.com").build();

    when(userRepository.existsEmail(user.getId(), user.getEmail())).thenReturn(true);

    Assertions.assertThrows(EmailAlreadyExistsException.class, () -> usersService.update(user));
  }

  @Test
  public void givenAExistentUser_whenExecuteFindByUsername_shouldReturnAUser() {

    User user =
        new UserBuilder().id(1L).username("user1").bio("user1 bio").email("user1@mail.com").build();

    Optional<User> userOptional = Optional.of(user);

    when(userRepository.findByUsernameOptional(user.getUsername())).thenReturn(userOptional);

    User result = usersService.findByUsername(user.getUsername());

    Assertions.assertNotNull(result);
  }

  @Test
  public void givenAInexistentUser_whenExecuteFindByUsername_shouldThrowsUserNotFoundException() {

    String username = "user";

    when(userRepository.findByUsernameOptional(username)).thenReturn(Optional.empty());

    Assertions.assertThrows(
        UserNotFoundException.class, () -> usersService.findByUsername(username));
  }
}
