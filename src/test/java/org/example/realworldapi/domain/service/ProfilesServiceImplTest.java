package org.example.realworldapi.domain.service;

import org.example.realworldapi.domain.entity.Profile;
import org.example.realworldapi.domain.entity.User;
import org.example.realworldapi.domain.entity.builder.UserBuilder;
import org.example.realworldapi.domain.service.impl.ProfilesServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProfilesServiceImplTest {

  private UsersService usersService;
  private ProfilesService profilesService;

  @BeforeEach
  private void beforeEach() {
    usersService = mock(UsersService.class);
    profilesService = new ProfilesServiceImpl(usersService);
  }

  @Test
  public void
      givenValidUsernameAndNullForExistentUserLoggedUserId_shouldReturnProfileWithFollowingFieldFalse() {
    String username = "user1";
    Long loggedUserId = null;

    User existingUser =
        new UserBuilder().id(1L).username(username).bio("bio").image("image").build();

    when(usersService.findByUsername(username)).thenReturn(existingUser);

    Profile result = profilesService.getProfile(username, loggedUserId);

    Assertions.assertEquals(existingUser.getUsername(), result.getUsername());
    Assertions.assertEquals(existingUser.getBio(), result.getBio());
    Assertions.assertEquals(existingUser.getImage(), result.getImage());
    Assertions.assertFalse(result.isFollowing());
  }

  @Test
  public void
      givenValidUsernameAndLoggedUserIdWithFollowingUsers_shouldReturnProfileWithFollowingFieldTrue() {
    String username = "user1";
    Long loggedUserId = 1L;

    User existingUser =
        new UserBuilder().id(2L).username(username).bio("bio").image("image").build();

    when(usersService.findByUsername(username)).thenReturn(existingUser);

    when(usersService.isFollowing(existingUser.getId(), loggedUserId)).thenReturn(true);

    Profile result = profilesService.getProfile(username, loggedUserId);

    Assertions.assertEquals(existingUser.getUsername(), result.getUsername());
    Assertions.assertEquals(existingUser.getBio(), result.getBio());
    Assertions.assertEquals(existingUser.getImage(), result.getImage());
    Assertions.assertTrue(result.isFollowing());
  }
}
