package org.example.realworldapi.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.test.junit.QuarkusTest;
import org.apache.http.HttpStatus;
import org.example.realworldapi.DatabaseIntegrationTest;
import org.example.realworldapi.domain.entity.User;
import org.example.realworldapi.domain.entity.UsersFollowers;
import org.example.realworldapi.domain.entity.UsersFollowersKey;
import org.example.realworldapi.domain.security.Role;
import org.example.realworldapi.domain.service.JWTService;
import org.example.realworldapi.util.UserUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.ws.rs.core.MediaType;

import static io.restassured.RestAssured.given;
import static org.example.realworldapi.constants.TestConstants.*;
import static org.hamcrest.Matchers.is;

@QuarkusTest
public class ProfilesResourceIntegrationTest extends DatabaseIntegrationTest {

  private final String PROFILES_PATH = API_PREFIX + "/profiles";

  @Inject private ObjectMapper objectMapper;

  @Inject private JWTService jwtService;

  @AfterEach
  public void afterEach() {
    clear();
  }

  @Test
  public void
      givenExistentUser_whenExecuteGetProfileEndpointWithoutAuth_shouldReturnAUserProfile() {

    User existentUser = createUser("user1", "user1@mail.com", "user123");

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

    User loggedUser = createUser("loggeduser", "loggeduser@mail.com", "user123", Role.USER);
    User user = createUser("user", "user@mail.com", "user123", Role.USER);

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

    User loggedUser = createUser("loggeduser", "loggeduser@mail.com", "user123", Role.USER);
    User user = createUser("user", "user@mail.com", "user123", Role.USER);

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

    User user = createUser("user", "user@mail.com", "user123", Role.USER);
    User loggedUser = createUser("loggeduser", "loggeduser@mail.com", "user123", Role.USER);

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

    User user = createUser("user", "user@mail.com", "user123", Role.USER);
    User loggedUser = createUser("loggeduser", "loggeduser@mail.com", "user123", Role.USER);

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

  private User createUser(String username, String email, String password, Role... role) {
    return transaction(
        () -> {
          User user = UserUtils.create(username, email, password);
          entityManager.persist(user);
          user.setToken(jwtService.sign(user.getId().toString(), role));
          entityManager.merge(user);
          return user;
        });
  }

  private void follow(User currentUser, User... followers) {

    transaction(
        () -> {
          User user = entityManager.find(User.class, currentUser.getId());

          for (User follower : followers) {
            UsersFollowersKey key = new UsersFollowersKey();
            key.setUser(user);
            key.setFollower(follower);

            UsersFollowers usersFollowers = new UsersFollowers();
            usersFollowers.setPrimaryKey(key);
            entityManager.persist(usersFollowers);
          }

          entityManager.persist(user);
        });
  }
}
