package org.example.realworldapi.domain.service;

import org.example.realworldapi.domain.entity.User;
import org.example.realworldapi.domain.exception.ExistingEmailException;
import org.example.realworldapi.domain.exception.InvalidPasswordException;
import org.example.realworldapi.domain.exception.UserNotFoundException;
import org.example.realworldapi.domain.repository.UsersRepository;
import org.example.realworldapi.domain.service.impl.UsersServiceImpl;
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

public class UsersServiceImplTest {

    private UsersRepository usersRepository;
    private UsersService usersService;

    @BeforeEach
    public void beforeEach() {
        usersRepository = mock(UsersRepository.class);
        usersService = new UsersServiceImpl(usersRepository);
    }

    @Test
    public void givenValidNewUserData_thenReturnAnCreatedUserWithFilledTokenField() {

        String username = "user";
        String email = "user@email.com";
        String password = "user123";

        User createdUser = new User();
        createdUser.setUsername(username);
        createdUser.setEmail(email);
        createdUser.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
        createdUser.setToken(UUID.randomUUID().toString());

        when(usersRepository.create(any(User.class))).thenReturn(Optional.of(createdUser));

        User resultUser = usersService.create(username, email, password);

        Assertions.assertNotNull(resultUser.getUsername());
        Assertions.assertNotNull(resultUser.getEmail());
        Assertions.assertNotNull(resultUser.getPassword());
        Assertions.assertNotNull(resultUser.getToken());

    }

    @Test
    public void whenExecuteCreateWithExistingEmail_shouldThrowsExistingEmailException(){

        String username = "user";
        String email = "user@email.com";
        String password = "user123";

       when(usersRepository.exists(email)).thenReturn(true);

        Assertions.assertThrows(ExistingEmailException.class, ()->
                usersService.create(username, email, password)
        );

    }

    @Test
    public void givenAnValidLoginInfo_thenReturnsAUser(){

        String email = "user1@mail.com";
        String password = "123";

        Optional<User> existingUser = Optional.of(UserUtils.create("user1", email, password));

        when(usersRepository.findByEmail(email)).thenReturn(existingUser);

        Optional<User> resultUser = usersService.login(email, password);

        Assertions.assertTrue(resultUser.isPresent());
        Assertions.assertEquals(existingUser.get(), resultUser.get());

    }

    @Test
    public void givenAInvalidEmail_thenUserNotFoundException(){

        String email = "user1@mail.com";
        String password = "123";

        when(usersRepository.findByEmail(email)).thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, ()->
            usersService.login(email, password)
        );

    }

    @Test
    public void givenAValidEmailAndAInvalidPassword_thenThrowsInvalidPasswordException(){

        String email = "user1@mail.com";
        String password = "123";

       Optional<User> existingUser = Optional.of(UserUtils.create("user1", email, password));

        when(usersRepository.findByEmail(email)).thenReturn(existingUser);

        Assertions.assertThrows(InvalidPasswordException.class, ()->
            usersService.login(email, "158")
        );

    }

    @Test
    public void givenAPersistedUser_whenExecuteFindById_shouldRetrieveUser(){

        User user = UserUtils.create("User1", "user1@mail.com", "user123");
        user.setId(1L);

        Optional<User> userResponse = Optional.of(user);

        when(usersRepository.findById(user.getId())).thenReturn(userResponse);

        Optional<User> result = usersService.findById(user.getId());

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(user.getId(), result.get().getId());

    }

    @Test
    public void givenANotPersistedUser_whenExecuteFindById_shouldThrowsResourceNotFoundException(){

        Long userId = 1L;

        when(usersRepository.findById(userId)).thenReturn(Optional.empty());

        Optional<User> result = usersService.findById(userId);

        Assertions.assertFalse(result.isPresent());

    }



}
