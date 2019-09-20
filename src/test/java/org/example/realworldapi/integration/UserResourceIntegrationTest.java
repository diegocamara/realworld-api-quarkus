package org.example.realworldapi.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.test.junit.QuarkusTest;
import org.apache.http.HttpStatus;
import org.example.realworldapi.DatabaseIntegrationTest;
import org.example.realworldapi.domain.entity.User;
import org.example.realworldapi.domain.security.Role;
import org.example.realworldapi.domain.service.JWTService;
import org.example.realworldapi.util.UserUtils;
import org.example.realworldapi.web.dto.UpdateUserDTO;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.ws.rs.core.MediaType;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;

@QuarkusTest
public class UserResourceIntegrationTest extends DatabaseIntegrationTest {

    public static final String AUTHORIZATION_HEADER_PREFIX = "Token ";
    public static final String AUTHORIZATION_HEADER = "Authorization";
    private final String API_PREFIX = "/api";
    private final String USER_RESOURCE_PATH = API_PREFIX + "/user";

    @Inject
    private ObjectMapper objectMapper;

    @Inject
    private JWTService jwtService;

    @AfterEach
    public void afterEach() {
        clear();
    }

    @Test
    public void givenAValidToken_whenExecuteGetUserEndpoint_shouldReturnLoggedInUser() {

        User user = createUser("user1", "user1@mail.com", "123", Role.USER);

        String authorizationHeader = AUTHORIZATION_HEADER_PREFIX + user.getToken();

        given()
                .header(AUTHORIZATION_HEADER, authorizationHeader)
                .contentType(MediaType.APPLICATION_JSON)
                .get(USER_RESOURCE_PATH)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("user.id", Matchers.nullValue(),
                        "user.password", Matchers.nullValue(),
                        "user.username", Matchers.notNullValue(),
                        "user.email", Matchers.notNullValue(),
                        "user.token", Matchers.notNullValue(),
                        "user.bio", Matchers.nullValue(),
                        "user.image", Matchers.nullValue());

    }

    @Test
    public void givenAInexistentUser_whenExecuteGetUserEndpoint_shouldReturn404NotFound() {

        String authorizationHeader = AUTHORIZATION_HEADER_PREFIX + jwtService.sign("1", Role.USER);

        given()
                .header(AUTHORIZATION_HEADER, authorizationHeader)
                .contentType(MediaType.APPLICATION_JSON)
                .get(USER_RESOURCE_PATH)
                .then()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .body("errors.body", hasItems("NOT_FOUND"));

    }

    @Test
    public void givenARequestWithoutAuthorizationHeader_whenExecuteGetUserEndpoint_shouldReturnUnauthorized() throws JsonProcessingException {

        User user = UserUtils.create("user1", "user1@mail.com", "123");

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(objectMapper.writeValueAsString(user))
                .get(USER_RESOURCE_PATH)
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("errors.body", hasItems("UNAUTHORIZED"));

    }

    @Test
    public void givenAExistentUser_whenExecuteUpdateUserEndpoint_shouldReturnUpdatedUser() throws JsonProcessingException {

        User user = createUser("user1", "user1@mail.com", "123", Role.USER);

        String authorizationHeader = AUTHORIZATION_HEADER_PREFIX + user.getToken();

        UpdateUserDTO updateUserDTO = new UpdateUserDTO();
        updateUserDTO.setUsername("user2");
        updateUserDTO.setEmail(user.getEmail());

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION_HEADER, authorizationHeader)
                .body(objectMapper.writeValueAsString(updateUserDTO))
                .put(USER_RESOURCE_PATH)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("user.id", Matchers.nullValue(),
                        "user.password", Matchers.nullValue(),
                        "user.username", is(updateUserDTO.getUsername()),
                        "user.email", Matchers.notNullValue(),
                        "user.token", Matchers.notNullValue(),
                        "user.bio", Matchers.nullValue(),
                        "user.image", Matchers.nullValue());


    }

    @Test
    public void givenAExistentUser_whenExecuteUpdateUserEndpointWithEmptyBody_shouldReturn422() throws JsonProcessingException {

        User user = createUser("user1", "user1@mail.com", "123", Role.USER);

        String authorizationHeader = AUTHORIZATION_HEADER_PREFIX + user.getToken();

        UpdateUserDTO updateUserDTO = new UpdateUserDTO();

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION_HEADER, authorizationHeader)
                .body(objectMapper.writeValueAsString(updateUserDTO))
                .put(USER_RESOURCE_PATH)
                .then()
                .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY)
                .body("errors.body", hasItems("At least one field must be not null"));


    }

    @Test
    public void givenAnotherExistingUsername_whenExecuteUpdateUserEndpoint_shouldReturn409() throws JsonProcessingException {

        User otherUser = createUser("user", "user@mail.com", "123", Role.USER);

        User currentUser = createUser("currentUser", "current@mail.com", "123", Role.USER);

        String authorizationHeader = AUTHORIZATION_HEADER_PREFIX + currentUser.getToken();

        UpdateUserDTO updateUserDTO = new UpdateUserDTO();
        updateUserDTO.setUsername(otherUser.getUsername());

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION_HEADER, authorizationHeader)
                .body(objectMapper.writeValueAsString(updateUserDTO))
                .put(USER_RESOURCE_PATH)
                .then()
                .statusCode(HttpStatus.SC_CONFLICT)
                .body("errors.body", hasItems("username already exists"));

    }

    @Test
    public void givenAnotherExistingEmail_whenExecuteUpdateUserEndpoint_shouldReturn409() throws JsonProcessingException {

        User otherUser = createUser("user", "user@mail.com", "123", Role.USER);

        User currentUser = createUser("currentUser", "current@mail.com", "123", Role.USER);

        String authorizationHeader = AUTHORIZATION_HEADER_PREFIX + currentUser.getToken();

        UpdateUserDTO updateUserDTO = new UpdateUserDTO();
        updateUserDTO.setEmail(otherUser.getEmail());

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION_HEADER, authorizationHeader)
                .body(objectMapper.writeValueAsString(updateUserDTO))
                .put(USER_RESOURCE_PATH)
                .then()
                .statusCode(HttpStatus.SC_CONFLICT)
                .body("errors.body", hasItems("email already exists"));

    }

    @Test
    public void givenAExistentUser_whenExecuteUpdateUserEndpointWithEmptyUsername_shouldReturn422() throws JsonProcessingException {

        User user = createUser("user1", "user1@mail.com", "123", Role.USER);

        String authorizationHeader = AUTHORIZATION_HEADER_PREFIX + user.getToken();

        UpdateUserDTO updateUserDTO = new UpdateUserDTO();
        updateUserDTO.setUsername("");

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION_HEADER, authorizationHeader)
                .body(objectMapper.writeValueAsString(updateUserDTO))
                .put(USER_RESOURCE_PATH)
                .then()
                .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY)
                .body("errors.body", hasItems("username must be not blank"));


    }

    @Test
    public void givenAExistentUser_whenExecuteUpdateUserEndpointWithBlankUsername_shouldReturn422() throws JsonProcessingException {

        User user = createUser("user1", "user1@mail.com", "123", Role.USER);

        String authorizationHeader = AUTHORIZATION_HEADER_PREFIX + user.getToken();

        UpdateUserDTO updateUserDTO = new UpdateUserDTO();
        updateUserDTO.setUsername(" ");

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION_HEADER, authorizationHeader)
                .body(objectMapper.writeValueAsString(updateUserDTO))
                .put(USER_RESOURCE_PATH)
                .then()
                .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY)
                .body("errors.body", hasItems("username must be not blank"));


    }

    private User createUser(String username, String email, String password, Role... role) {
        return transaction(() -> {
            User user = UserUtils.create(username, email, password);
            entityManager.persist(user);
            user.setToken(jwtService.sign(user.getId().toString(), role));
            entityManager.merge(user);
            return user;
        });
    }

}
