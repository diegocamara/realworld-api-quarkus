package org.example.realworldapi.domain.service;

import org.apache.http.HttpStatus;
import org.example.realworldapi.domain.entity.User;
import org.example.realworldapi.domain.exception.ConflictException;
import org.example.realworldapi.domain.exception.UnauthorizedException;
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

        when(usersRepository.create(any(User.class))).thenReturn(createdUser);

        User resultUser = usersService.create(username, email, password);

        Assertions.assertNotNull(resultUser.getUsername());
        Assertions.assertNotNull(resultUser.getEmail());
        Assertions.assertNotNull(resultUser.getPassword());
        Assertions.assertNotNull(resultUser.getToken());

    }

    @Test
    public void whenExecuteCreateWithExistingEmail_shouldThrowsConflictException(){

        String username = "user";
        String email = "user@email.com";
        String password = "user123";

       when(usersRepository.exists(email)).thenReturn(true);

        ConflictException conflictException = Assertions.assertThrows(ConflictException.class, ()->{
           usersService.create(username, email, password);
        });

        Assertions.assertEquals(HttpStatus.SC_CONFLICT, conflictException.getStatusCode());
        Assertions.assertEquals("Conflict", conflictException.getMessage());

    }

    @Test
    public void givenAnValidLoginInfo_thenReturnsAUser(){

        String email = "user1@mail.com";
        String password = "123";

        Optional<User> existingUser = Optional.of(UserUtils.create("user1", email, password));

        when(usersRepository.findByEmail(email)).thenReturn(existingUser);

        User resultUser = usersService.login(email, password);

        Assertions.assertEquals(existingUser.get(), resultUser);

    }

    @Test
    public void givenAInvalidEmail_thenReturnsAUnauthorizedException(){

        String email = "user1@mail.com";
        String password = "123";

        when(usersRepository.findByEmail(email)).thenReturn(Optional.empty());

        UnauthorizedException unauthorizedException = Assertions.assertThrows(UnauthorizedException.class, () -> {
            usersService.login(email, password);
        });

        Assertions.assertEquals(HttpStatus.SC_UNAUTHORIZED, unauthorizedException.getStatusCode());
        Assertions.assertEquals("Unauthorized", unauthorizedException.getMessage());

    }

    @Test
    public void givenAValidEmailAndAInvalidPassword_thenReturnsAUnauthorizedException(){

        String email = "user1@mail.com";
        String password = "123";

       Optional<User> existingUser = Optional.of(UserUtils.create("user1", email, password));

        when(usersRepository.findByEmail(email)).thenReturn(existingUser);

        UnauthorizedException unauthorizedException = Assertions.assertThrows(UnauthorizedException.class, () -> {
            usersService.login(email, "158");
        });

        Assertions.assertEquals(HttpStatus.SC_UNAUTHORIZED, unauthorizedException.getStatusCode());
        Assertions.assertEquals("Unauthorized", unauthorizedException.getMessage());

    }

}
