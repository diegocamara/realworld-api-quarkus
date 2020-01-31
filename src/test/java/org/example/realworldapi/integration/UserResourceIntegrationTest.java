package org.example.realworldapi.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.quarkus.test.junit.QuarkusTest;
import org.apache.http.HttpStatus;
import org.example.realworldapi.AbstractIntegrationTest;
import org.example.realworldapi.domain.model.entity.User;
import org.example.realworldapi.infrastructure.web.model.request.UpdateUserRequest;
import org.example.realworldapi.util.UserUtils;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.MediaType;

import static io.restassured.RestAssured.given;
import static org.example.realworldapi.constants.TestConstants.*;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class UserResourceIntegrationTest extends AbstractIntegrationTest {

  private final String USER_RESOURCE_PATH = API_PREFIX + "/user";

  @Test
  public void givenAValidToken_whenExecuteGetUserEndpoint_shouldReturnLoggedInUser() {

    User user = createUser("user1", "user1@mail.com", "bio", "image", "123");

    given()
        .header(AUTHORIZATION_HEADER, AUTHORIZATION_HEADER_VALUE_PREFIX + user.getToken())
        .contentType(MediaType.APPLICATION_JSON)
        .get(USER_RESOURCE_PATH)
        .then()
        .statusCode(HttpStatus.SC_OK)
        .body(
            "user.id",
            Matchers.nullValue(),
            "user.password",
            Matchers.nullValue(),
            "user.username",
            Matchers.notNullValue(),
            "user.email",
            Matchers.notNullValue(),
            "user.token",
            Matchers.notNullValue(),
            "user.bio",
            is(user.getBio()),
            "user.image",
            is(user.getImage()));
  }

  @Test
  public void givenAInexistentUser_whenExecuteGetUserEndpoint_shouldReturn404NotFound() {

    String authorizationHeader =
        AUTHORIZATION_HEADER_VALUE_PREFIX + tokenProvider.createUserToken("1");

    given()
        .header(AUTHORIZATION_HEADER, authorizationHeader)
        .contentType(MediaType.APPLICATION_JSON)
        .get(USER_RESOURCE_PATH)
        .then()
        .statusCode(HttpStatus.SC_NOT_FOUND)
        .body("errors.body", hasItems("NOT_FOUND"));
  }

  @Test
  public void
      givenARequestWithoutAuthorizationHeader_whenExecuteGetUserEndpoint_shouldReturnUnauthorized()
          throws JsonProcessingException {

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
  public void givenAExistentUser_whenExecuteUpdateUserEndpoint_shouldReturnUpdatedUser()
      throws JsonProcessingException {

    User user = createUser("user1", "user1@mail.com", "bio", "image", "123");

    String authorizationHeader = AUTHORIZATION_HEADER_VALUE_PREFIX + user.getToken();

    UpdateUserRequest updateUserRequest = new UpdateUserRequest();
    updateUserRequest.setUsername("user2");
    updateUserRequest.setEmail(user.getEmail());

    given()
        .contentType(MediaType.APPLICATION_JSON)
        .header(AUTHORIZATION_HEADER, authorizationHeader)
        .body(objectMapper.writeValueAsString(updateUserRequest))
        .put(USER_RESOURCE_PATH)
        .then()
        .statusCode(HttpStatus.SC_OK)
        .body(
            "user.id",
            Matchers.nullValue(),
            "user.password",
            Matchers.nullValue(),
            "user.username",
            is(updateUserRequest.getUsername()),
            "user.email",
            Matchers.notNullValue(),
            "user.token",
            Matchers.notNullValue(),
            "user.bio",
            is(user.getBio()),
            "user.image",
            is(user.getImage()));
  }

  @Test
  public void givenAExistentUser_whenExecuteUpdateUserEndpointWithEmptyBody_shouldReturn422()
      throws JsonProcessingException {

    User user = createUser("user1", "user1@mail.com", "bio", "image", "123");

    String authorizationHeader = AUTHORIZATION_HEADER_VALUE_PREFIX + user.getToken();

    UpdateUserRequest updateUserRequest = new UpdateUserRequest();

    given()
        .contentType(MediaType.APPLICATION_JSON)
        .header(AUTHORIZATION_HEADER, authorizationHeader)
        .body(objectMapper.writeValueAsString(updateUserRequest))
        .put(USER_RESOURCE_PATH)
        .then()
        .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY)
        .body("errors.body", hasItems("At least one field must be not null"));
  }

  @Test
  public void givenAnotherExistingUsername_whenExecuteUpdateUserEndpoint_shouldReturn409()
      throws JsonProcessingException {

    User otherUser = createUser("user", "user@mail.com", "bio", "image", "123");

    User currentUser = createUser("currentUser", "current@mail.com", "bio", "image", "123");

    String authorizationHeader = AUTHORIZATION_HEADER_VALUE_PREFIX + currentUser.getToken();

    UpdateUserRequest updateUserRequest = new UpdateUserRequest();
    updateUserRequest.setUsername(otherUser.getUsername());

    given()
        .contentType(MediaType.APPLICATION_JSON)
        .header(AUTHORIZATION_HEADER, authorizationHeader)
        .body(objectMapper.writeValueAsString(updateUserRequest))
        .put(USER_RESOURCE_PATH)
        .then()
        .statusCode(HttpStatus.SC_CONFLICT)
        .body("errors.body", hasItems("username already exists"));
  }

  @Test
  public void givenAnotherExistingEmail_whenExecuteUpdateUserEndpoint_shouldReturn409()
      throws JsonProcessingException {

    User otherUser = createUser("user", "user@mail.com", "bio", "image", "123");

    User currentUser = createUser("currentUser", "current@mail.com", "bio", "image", "123");

    String authorizationHeader = AUTHORIZATION_HEADER_VALUE_PREFIX + currentUser.getToken();

    UpdateUserRequest updateUserRequest = new UpdateUserRequest();
    updateUserRequest.setEmail(otherUser.getEmail());

    given()
        .contentType(MediaType.APPLICATION_JSON)
        .header(AUTHORIZATION_HEADER, authorizationHeader)
        .body(objectMapper.writeValueAsString(updateUserRequest))
        .put(USER_RESOURCE_PATH)
        .then()
        .statusCode(HttpStatus.SC_CONFLICT)
        .body("errors.body", hasItems("email already exists"));
  }

  @Test
  public void givenAExistentUser_whenExecuteUpdateUserEndpointWithEmptyUsername_shouldReturn422()
      throws JsonProcessingException {

    User user = createUser("user1", "user1@mail.com", "bio", "image", "123");

    String authorizationHeader = AUTHORIZATION_HEADER_VALUE_PREFIX + user.getToken();

    UpdateUserRequest updateUserRequest = new UpdateUserRequest();
    updateUserRequest.setUsername("");

    given()
        .contentType(MediaType.APPLICATION_JSON)
        .header(AUTHORIZATION_HEADER, authorizationHeader)
        .body(objectMapper.writeValueAsString(updateUserRequest))
        .put(USER_RESOURCE_PATH)
        .then()
        .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY)
        .body("errors.body", hasItems("username must be not blank"));
  }

  @Test
  public void givenAExistentUser_whenExecuteUpdateUserEndpointWithInvalidEmail_shouldReturn422()
      throws JsonProcessingException {

    User user = createUser("user1", "user1@mail.com", "bio", "image", "123");

    String authorizationHeader = AUTHORIZATION_HEADER_VALUE_PREFIX + user.getToken();

    UpdateUserRequest updateUserRequest = new UpdateUserRequest();
    updateUserRequest.setEmail("email");

    given()
        .contentType(MediaType.APPLICATION_JSON)
        .header(AUTHORIZATION_HEADER, authorizationHeader)
        .body(objectMapper.writeValueAsString(updateUserRequest))
        .put(USER_RESOURCE_PATH)
        .then()
        .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY)
        .body(
            "errors.body",
            hasSize(1),
            "errors.body",
            hasItems("must be a well-formed email address"));
  }

  @Test
  public void givenAExistentUser_whenExecuteUpdateUserEndpointWithBlankUsername_shouldReturn422()
      throws JsonProcessingException {

    User user = createUser("user1", "user1@mail.com", "bio", "image", "123");

    String authorizationHeader = AUTHORIZATION_HEADER_VALUE_PREFIX + user.getToken();

    UpdateUserRequest updateUserRequest = new UpdateUserRequest();
    updateUserRequest.setUsername(" ");

    given()
        .contentType(MediaType.APPLICATION_JSON)
        .header(AUTHORIZATION_HEADER, authorizationHeader)
        .body(objectMapper.writeValueAsString(updateUserRequest))
        .put(USER_RESOURCE_PATH)
        .then()
        .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY)
        .body("errors.body", hasItems("username must be not blank"));
  }
}
