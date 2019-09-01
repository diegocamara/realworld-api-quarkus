package org.example.realworldapi.domain.service;

import org.example.realworldapi.domain.entity.User;
import org.example.realworldapi.domain.repository.UsersRepository;
import org.example.realworldapi.domain.service.impl.UsersServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

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

}
