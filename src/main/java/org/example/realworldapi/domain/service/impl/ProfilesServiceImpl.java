package org.example.realworldapi.domain.service.impl;

import org.example.realworldapi.domain.entity.Profile;
import org.example.realworldapi.domain.entity.User;
import org.example.realworldapi.domain.builder.ProfileBuilder;
import org.example.realworldapi.domain.service.ProfilesService;
import org.example.realworldapi.domain.service.UsersFollowersService;
import org.example.realworldapi.domain.service.UsersService;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

@ApplicationScoped
public class ProfilesServiceImpl implements ProfilesService {

  private UsersService usersService;
  private UsersFollowersService usersFollowersService;

  public ProfilesServiceImpl(
      UsersService usersService, UsersFollowersService usersFollowersService) {
    this.usersService = usersService;
    this.usersFollowersService = usersFollowersService;
  }

  @Override
  @Transactional
  public Profile getProfile(String username, Long loggedUserId) {
    User existentUser = usersService.findByUsername(username);
    boolean isFollowing = usersFollowersService.isFollowing(loggedUserId, existentUser.getId());
    ProfileBuilder profileBuilder = new ProfileBuilder().fromUser(existentUser);
    profileBuilder.following(isFollowing);
    return profileBuilder.build();
  }

  @Override
  @Transactional
  public Profile follow(Long loggedUserId, String username) {
    User loggedUser = usersService.findById(loggedUserId);
    User userToFollow = usersService.findByUsername(username);
    usersFollowersService.insertOrUpdate(loggedUser, userToFollow);
    return getProfile(username, loggedUserId);
  }

  @Override
  @Transactional
  public Profile unfollow(Long loggedUserId, String username) {
    User loggedUser = usersService.findById(loggedUserId);
    User userToUnfollow = usersService.findByUsername(username);
    usersFollowersService.delete(loggedUser, userToUnfollow);
    return getProfile(username, loggedUserId);
  }
}
