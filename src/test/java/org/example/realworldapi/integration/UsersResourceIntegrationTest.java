package org.example.realworldapi.integration;

import static io.restassured.RestAssured.given;
import static org.example.realworldapi.constants.TestConstants.API_PREFIX;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.MediaType;
import org.apache.http.HttpStatus;
import org.example.realworldapi.AbstractIntegrationTest;
import org.example.realworldapi.application.web.model.request.LoginRequest;
import org.example.realworldapi.application.web.model.request.NewUserRequest;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class UsersResourceIntegrationTest extends AbstractIntegrationTest {

  private final String USERS_RESOURCE_PATH = API_PREFIX + "/users";
  private final String LOGIN_PATH = USERS_RESOURCE_PATH + "/login";

  @Test
  public void
      givenAValidUser_whenCallingRegisterUserEndpoint_thenReturnAnUserWithTokenFieldAndCode201()
          throws JsonProcessingException {

    NewUserRequest newUser = new NewUserRequest();
    newUser.setUsername("user");
    newUser.setEmail("user@mail.com");
    newUser.setPassword("user123");

    given()
        .contentType(MediaType.APPLICATION_JSON)
        .body(objectMapper.writeValueAsString(newUser))
        .when()
        .post(USERS_RESOURCE_PATH)
        .then()
        .statusCode(HttpStatus.SC_CREATED)
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
            Matchers.nullValue(),
            "user.image",
            Matchers.nullValue());
  }

  @Test
  public void
      givenAPersistedUser_whenCallingRegisterUserEndpointWithExistingEmail_thenReturnCode409()
          throws JsonProcessingException {

    String userPassword = "123";

    final var user = createUserEntity("user1", "user1@mail.com", "bio", "image", userPassword);

    NewUserRequest newUser = new NewUserRequest();
    newUser.setUsername("user2");
    newUser.setEmail(user.getEmail());
    newUser.setPassword("user123");

    given()
        .contentType(MediaType.APPLICATION_JSON)
        .body(objectMapper.writeValueAsString(newUser))
        .when()
        .post(USERS_RESOURCE_PATH)
        .then()
        .statusCode(HttpStatus.SC_CONFLICT)
        .body("errors.body", hasItems("email already exists"));
  }

  @Test
  public void
      givenAPersistedUser_whenCallingRegisterUserEndpointWithExistingUsername_thenReturnCode409()
          throws JsonProcessingException {

    String userPassword = "123";

    final var user = createUserEntity("user1", "user1@mail.com", "bio", "image", userPassword);

    NewUserRequest newUser = new NewUserRequest();
    newUser.setUsername(user.getUsername());
    newUser.setEmail("user2@mail.com");
    newUser.setPassword("user123");

    given()
        .contentType(MediaType.APPLICATION_JSON)
        .body(objectMapper.writeValueAsString(newUser))
        .when()
        .post(USERS_RESOURCE_PATH)
        .then()
        .statusCode(HttpStatus.SC_CONFLICT)
        .body("errors.body", hasItems("username already exists"));
  }

  @Test
  public void givenAnInvalidUser_thenReturnErrorsWith422Code() throws JsonProcessingException {

    NewUserRequest newUser = new NewUserRequest();

    given()
        .contentType(MediaType.APPLICATION_JSON)
        .body(objectMapper.writeValueAsString(newUser))
        .when()
        .post(USERS_RESOURCE_PATH)
        .then()
        .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY)
        .body(
            "errors.body",
            hasSize(3),
            "errors.body",
            hasItems(
                "username must be not blank",
                "email must be not blank",
                "password must be not blank"));
  }

  @Test
  public void givenAnInvalidEmail_thenReturnErrorsWith422Code() throws JsonProcessingException {

    NewUserRequest newUser = new NewUserRequest();
    newUser.setUsername("username");
    newUser.setEmail("email");
    newUser.setPassword("user123");

    given()
        .contentType(MediaType.APPLICATION_JSON)
        .body(objectMapper.writeValueAsString(newUser))
        .when()
        .post(USERS_RESOURCE_PATH)
        .then()
        .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY)
        .body(
            "errors.body",
            hasSize(1),
            "errors.body",
            hasItems("must be a well-formed email address"));
  }

  @Test
  public void givenAInvalidLogin_whenExecuteLoginEndpoint_shouldReturnErrorsWith422Code()
      throws JsonProcessingException {

    LoginRequest loginRequest = new LoginRequest();

    given()
        .contentType(MediaType.APPLICATION_JSON)
        .body(objectMapper.writeValueAsString(loginRequest))
        .when()
        .post(LOGIN_PATH)
        .then()
        .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY)
        .body("errors.body", hasItems("email must be not blank", "password must be not blank"));
  }

  @Test
  public void givenAInvalidLoginEmail_whenExecuteLoginEndpoint_shouldReturnUnauthorized()
      throws JsonProcessingException {

    String userPassword = "123";

    final var user = createUserEntity("user1", "user1@mail.com", "bio", "image", userPassword);

    LoginRequest loginRequest = new LoginRequest();
    loginRequest.setEmail("user2@mail.com");
    loginRequest.setPassword(userPassword);

    given()
        .contentType(MediaType.APPLICATION_JSON)
        .body(objectMapper.writeValueAsString(loginRequest))
        .when()
        .post(LOGIN_PATH)
        .then()
        .statusCode(HttpStatus.SC_UNAUTHORIZED)
        .body("errors.body", hasItems("Unauthorized"));
  }

  @Test
  public void givenAInvalidLoginPassword_whenExecuteLoginEndpoint_shouldReturnUnauthorized()
      throws JsonProcessingException {

    final var user = createUserEntity("user1", "user1@mail.com", "123", "bio", "image");

    LoginRequest loginRequest = new LoginRequest();
    loginRequest.setEmail(user.getEmail());
    loginRequest.setPassword("145");

    given()
        .contentType(MediaType.APPLICATION_JSON)
        .body(objectMapper.writeValueAsString(loginRequest))
        .when()
        .post(LOGIN_PATH)
        .then()
        .statusCode(HttpStatus.SC_UNAUTHORIZED)
        .body("errors.body", hasItems("Unauthorized"));
  }
}
