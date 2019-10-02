package org.example.realworldapi.domain.service.impl;

import org.example.realworldapi.domain.builder.ProfileBuilder;
import org.example.realworldapi.domain.entity.Profile;
import org.example.realworldapi.domain.entity.persistent.User;
import org.example.realworldapi.domain.entity.persistent.UsersFollowers;
import org.example.realworldapi.domain.entity.persistent.UsersFollowersKey;
import org.example.realworldapi.domain.repository.UsersFollowersRepository;
import org.example.realworldapi.domain.service.ProfilesService;
import org.example.realworldapi.domain.service.UsersService;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

@ApplicationScoped
public class ProfilesServiceImpl implements ProfilesService {

  private UsersService usersService;
  private UsersFollowersRepository usersFollowersRepository;

  public ProfilesServiceImpl(
      UsersService usersService, UsersFollowersRepository usersFollowersRepository) {
    this.usersService = usersService;
    this.usersFollowersRepository = usersFollowersRepository;
  }

  @Override
  @Transactional
  public Profile getProfile(String username, Long loggedUserId) {
    User existentUser = usersService.findByUsername(username);

    ProfileBuilder profileBuilder = new ProfileBuilder().fromUser(existentUser);

    if (loggedUserId != null) {
      profileBuilder.following(
          usersFollowersRepository.isFollowing(loggedUserId, existentUser.getId()));
    }

    return profileBuilder.build();
  }

  @Override
  @Transactional
  public Profile follow(Long loggedUserId, String username) {
    User loggedUser = usersService.findById(loggedUserId);
    User userToFollow = usersService.findByUsername(username);
    usersFollowersRepository.insertOrUpdate(getUsersFollowers(loggedUser, userToFollow));
    return getProfile(username, loggedUserId);
  }

  @Override
  @Transactional
  public Profile unfollow(Long loggedUserId, String username) {
    User loggedUser = usersService.findById(loggedUserId);
    User userToUnfollow = usersService.findByUsername(username);
    UsersFollowers usersFollowers =
        usersFollowersRepository.findByKey(getUsersFollowersKey(loggedUser, userToUnfollow));
    usersFollowersRepository.delete(usersFollowers);
    return getProfile(username, loggedUserId);
  }

  private UsersFollowers getUsersFollowers(User user, User follower) {
    UsersFollowersKey primaryKey = getUsersFollowersKey(user, follower);
    UsersFollowers usersFollowers = new UsersFollowers();
    usersFollowers.setPrimaryKey(primaryKey);
    return usersFollowers;
  }

  private UsersFollowersKey getUsersFollowersKey(User user, User follower) {
    UsersFollowersKey primaryKey = new UsersFollowersKey();
    primaryKey.setUser(user);
    primaryKey.setFollower(follower);
    return primaryKey;
  }
}
