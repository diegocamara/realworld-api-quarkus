package org.example.realworldapi.integration;

import io.quarkus.test.junit.QuarkusTest;
import org.apache.http.HttpStatus;
import org.example.realworldapi.AbstractIntegrationTest;
import org.example.realworldapi.domain.model.entity.User;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.MediaType;

import static io.restassured.RestAssured.given;
import static org.example.realworldapi.constants.TestConstants.*;
import static org.hamcrest.Matchers.is;

@QuarkusTest
public class ProfilesResourceIntegrationTest extends AbstractIntegrationTest {

  private final String PROFILES_PATH = API_PREFIX + "/profiles";

  @Test
  public void
      givenExistentUser_whenExecuteGetProfileEndpointWithoutAuth_shouldReturnAUserProfile() {

    User existentUser = createUser("user1", "user1@mail.com", "bio", "image", "user123");

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

    User loggedUser = createUser("loggeduser", "loggeduser@mail.com", "bio", "image", "user123");
    User user = createUser("user", "user@mail.com", "bio", "image", "user123");

    follow(loggedUser, user);

    given()
        .contentType(MediaType.APPLICATION_JSON)
        .header(AUTHORIZATION_HEADER, AUTHORIZATION_HEADER_VALUE_PREFIX + loggedUser.getToken())
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

    User loggedUser = createUser("loggeduser", "loggeduser@mail.com", "bio", "image", "user123");
    User user = createUser("user", "user@mail.com", "bio", "image", "user123");

    given()
        .contentType(MediaType.APPLICATION_JSON)
        .header(AUTHORIZATION_HEADER, AUTHORIZATION_HEADER_VALUE_PREFIX + loggedUser.getToken())
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

    User user = createUser("user", "user@mail.com", "bio", "image", "user123");
    User loggedUser = createUser("loggeduser", "loggeduser@mail.com", "bio", "image", "user123");

    given()
        .contentType(MediaType.APPLICATION_JSON)
        .header(AUTHORIZATION_HEADER, AUTHORIZATION_HEADER_VALUE_PREFIX + loggedUser.getToken())
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

    User user = createUser("user", "user@mail.com", "bio", "image", "user123");
    User loggedUser = createUser("loggeduser", "loggeduser@mail.com", "bio", "image", "user123");

    follow(loggedUser, user);

    given()
        .contentType(MediaType.APPLICATION_JSON)
        .header(AUTHORIZATION_HEADER, AUTHORIZATION_HEADER_VALUE_PREFIX + loggedUser.getToken())
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
