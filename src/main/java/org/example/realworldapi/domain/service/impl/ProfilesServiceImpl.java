package org.example.realworldapi.domain.service.impl;

import org.example.realworldapi.domain.entity.Profile;
import org.example.realworldapi.domain.entity.User;
import org.example.realworldapi.domain.entity.builder.ProfileBuilder;
import org.example.realworldapi.domain.service.ProfilesService;
import org.example.realworldapi.domain.service.UsersService;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProfilesServiceImpl implements ProfilesService {

  private UsersService usersService;

  public ProfilesServiceImpl(UsersService usersService) {
    this.usersService = usersService;
  }

  @Override
  public Profile getProfile(String username, Long loggedUserId) {

    User existentUser = usersService.findByUsername(username);
    boolean isFollowing = usersService.isFollowing(existentUser.getId(), loggedUserId);
    ProfileBuilder profileBuilder = new ProfileBuilder().fromUser(existentUser);
    profileBuilder.following(isFollowing);
    return profileBuilder.build();
  }
}
