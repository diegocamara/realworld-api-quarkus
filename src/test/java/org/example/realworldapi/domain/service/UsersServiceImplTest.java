package org.example.realworldapi.domain.service;

import org.junit.jupiter.api.BeforeEach;

import static org.mockito.Mockito.mock;

public class UsersServiceImplTest {

    private UsersService usersService;

    @BeforeEach
    public void beforeEach() {
        this.usersService = mock(UsersService.class);

    }

}
