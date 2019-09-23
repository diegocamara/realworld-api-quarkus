package org.example.realworldapi.domain.service;

import org.example.realworldapi.domain.builder.UserBuilder;
import org.example.realworldapi.domain.entity.Profile;
import org.example.realworldapi.domain.entity.persistent.User;
import org.example.realworldapi.domain.repository.UsersFollowersRepository;
import org.example.realworldapi.domain.resource.service.ProfilesService;
import org.example.realworldapi.domain.resource.service.UsersService;
import org.example.realworldapi.domain.resource.service.impl.ProfilesServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProfilesServiceImplTest {

  private UsersService usersService;
  private UsersFollowersRepository usersFollowersRepository;
  private ProfilesService profilesService;

  @BeforeEach
  private void beforeEach() {
    usersService = mock(UsersService.class);
    usersFollowersRepository = mock(UsersFollowersRepository.class);
    profilesService = new ProfilesServiceImpl(usersService, usersFollowersRepository);
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
      givenValidUsernameAndLoggedUserIdWithFollowers_shouldReturnProfileWithFollowingFieldTrue() {
    String username = "user1";
    Long loggedUserId = 1L;

    User existingUser =
        new UserBuilder().id(2L).username(username).bio("bio").image("image").build();

    when(usersService.findByUsername(username)).thenReturn(existingUser);

    when(usersFollowersRepository.isFollowing(loggedUserId, existingUser.getId())).thenReturn(true);

    Profile result = profilesService.getProfile(username, loggedUserId);

    Assertions.assertEquals(existingUser.getUsername(), result.getUsername());
    Assertions.assertEquals(existingUser.getBio(), result.getBio());
    Assertions.assertEquals(existingUser.getImage(), result.getImage());
    Assertions.assertTrue(result.isFollowing());
  }
}
