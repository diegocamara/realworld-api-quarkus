package org.example.realworldapi.integration;

import static io.restassured.RestAssured.given;
import static org.example.realworldapi.constants.TestConstants.*;
import static org.hamcrest.Matchers.is;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.MediaType;
import org.apache.http.HttpStatus;
import org.example.realworldapi.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class ProfilesResourceIntegrationTest extends AbstractIntegrationTest {

  private final String PROFILES_PATH = API_PREFIX + "/profiles";

  @Test
  public void
      givenExistentUser_whenExecuteGetProfileEndpointWithoutAuth_shouldReturnAUserProfile() {

    final var existentUser = createUserEntity("user1", "user1@mail.com", "bio", "image", "user123");

    given()
        .contentType(MediaType.APPLICATION_JSON)
        .get(PROFILES_PATH + "/" + existentUser.getUsername())
        .then()
        .statusCode(HttpStatus.SC_OK)
        .body(
            "profile.size()",
            is(4),
            "profile.username",
            is(existentUser.getUsername()),
            "profile.bio",
            is(existentUser.getBio()),
            "profile.image",
            is(existentUser.getImage()),
            "profile.following",
            is(false));
  }

  @Test
  public void
      givenExistentUserWithFollows_whenExecuteGetProfileEndpointWithAuth_shouldReturnAUserProfileWithFollowingTrue() {

    final var loggedUser =
        createUserEntity("loggeduser", "loggeduser@mail.com", "bio", "image", "user123");
    final var user = createUserEntity("user", "user@mail.com", "bio", "image", "user123");

    follow(loggedUser, user);

    given()
        .contentType(MediaType.APPLICATION_JSON)
        .header(AUTHORIZATION_HEADER, AUTHORIZATION_HEADER_VALUE_PREFIX + token(loggedUser))
        .get(PROFILES_PATH + "/" + user.getUsername())
        .then()
        .statusCode(HttpStatus.SC_OK)
        .body(
            "profile.size()",
            is(4),
            "profile.username",
            is(user.getUsername()),
            "profile.bio",
            is(user.getBio()),
            "profile.image",
            is(user.getImage()),
            "profile.following",
            is(true));
  }

  @Test
  public void
      givenExistentUserWithoutFollows_whenExecuteGetProfileEndpointWithAuth_shouldReturnAUserProfileWithFollowingFalse() {

    final var loggedUser =
        createUserEntity("loggeduser", "loggeduser@mail.com", "bio", "image", "user123");
    final var user = createUserEntity("user", "user@mail.com", "bio", "image", "user123");

    given()
        .contentType(MediaType.APPLICATION_JSON)
        .header(AUTHORIZATION_HEADER, AUTHORIZATION_HEADER_VALUE_PREFIX + token(loggedUser))
        .get(PROFILES_PATH + "/" + user.getUsername())
        .then()
        .statusCode(HttpStatus.SC_OK)
        .body(
            "profile.size()",
            is(4),
            "profile.username",
            is(user.getUsername()),
            "profile.bio",
            is(user.getBio()),
            "profile.image",
            is(user.getImage()),
            "profile.following",
            is(false));
  }

  @Test
  public void
      givenExistentUsers_whenExecuteFollowEndpoint_shouldReturnProfileWithFollowingFieldTrue() {

    final var user = createUserEntity("user", "user@mail.com", "bio", "image", "user123");
    final var loggedUser =
        createUserEntity("loggeduser", "loggeduser@mail.com", "bio", "image", "user123");

    given()
        .contentType(MediaType.APPLICATION_JSON)
        .header(AUTHORIZATION_HEADER, AUTHORIZATION_HEADER_VALUE_PREFIX + token(loggedUser))
        .post(PROFILES_PATH + "/" + user.getUsername() + "/follow")
        .then()
        .statusCode(HttpStatus.SC_OK)
        .body(
            "profile.size()",
            is(4),
            "profile.username",
            is(user.getUsername()),
            "profile.bio",
            is(user.getBio()),
            "profile.image",
            is(user.getImage()),
            "profile.following",
            is(true));
  }

  @Test
  public void
      givenExistentUserWithFollower_whenExecuteUnfollowEndpoint_shouldReturnProfileWithFollowingFieldFalse() {

    final var user = createUserEntity("user", "user@mail.com", "bio", "image", "user123");
    final var loggedUser =
        createUserEntity("loggeduser", "loggeduser@mail.com", "bio", "image", "user123");

    follow(loggedUser, user);

    given()
        .contentType(MediaType.APPLICATION_JSON)
        .header(AUTHORIZATION_HEADER, AUTHORIZATION_HEADER_VALUE_PREFIX + token(loggedUser))
        .delete(PROFILES_PATH + "/" + user.getUsername() + "/follow")
        .then()
        .statusCode(HttpStatus.SC_OK)
        .body(
            "profile.size()",
            is(4),
            "profile.username",
            is(user.getUsername()),
            "profile.bio",
            is(user.getBio()),
            "profile.image",
            is(user.getImage()),
            "profile.following",
            is(false));
  }
}
