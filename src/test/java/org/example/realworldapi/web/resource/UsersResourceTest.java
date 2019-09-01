package org.example.realworldapi.web.resource;

import org.example.realworldapi.domain.entity.User;
import org.example.realworldapi.domain.service.UsersService;
import org.example.realworldapi.web.dto.NewUserDTO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.ws.rs.core.Response;
import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Response.class)
public class UsersResourceTest {

    private UsersService usersService;
    private UsersResource usersResource;

    @Before
    public void beforeEach() {
        usersService = mock(UsersService.class);
        usersResource = new UsersResource(usersService);
    }

    @Test
    public void givenAnValidNewUserDTO_thenReturnAnUserWithFilledToken() {

        NewUserDTO newUser = new NewUserDTO();
        newUser.setUsername("user");
        newUser.setEmail("user@mail.com");
        newUser.setPassword("user123");

        User createdUser = new User();
        createdUser.setToken(UUID.randomUUID().toString());

        when(usersService.create(newUser.getUsername(), newUser.getEmail(), newUser.getPassword()))
                .thenReturn(createdUser);

        Response response = Response.ok(createdUser).status(Response.Status.CREATED).build();

        Response.ResponseBuilder responseBuilder = mock(Response.ResponseBuilder.class);

        when(responseBuilder.status(Response.Status.CREATED)).thenReturn(responseBuilder);

        when(responseBuilder.build()).thenReturn(response);

        mockStatic(Response.class);

        when(Response.ok(createdUser)).thenReturn(responseBuilder);

        Response resultResponse = usersResource.create(newUser);
        User resultUser = (User) resultResponse.getEntity();

        Assert.assertEquals(createdUser, resultUser);
        Assert.assertNotNull(resultUser.getToken());

    }
}
