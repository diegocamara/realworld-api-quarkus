package org.example.realworldapi.domain.application;

import org.example.realworldapi.domain.application.data.ProfileData;
import org.example.realworldapi.domain.model.builder.ProfileBuilder;
import org.example.realworldapi.domain.model.entity.User;
import org.example.realworldapi.domain.model.entity.UsersFollowers;
import org.example.realworldapi.domain.model.entity.UsersFollowersKey;
import org.example.realworldapi.domain.model.repository.UsersFollowersRepository;
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
  public ProfileData getProfile(String username, Long loggedUserId) {
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
  public ProfileData follow(Long loggedUserId, String username) {
    User loggedUser = usersService.findById(loggedUserId);
    User userToFollow = usersService.findByUsername(username);
    usersFollowersRepository.create(getUsersFollowers(loggedUser, userToFollow));
    return getProfile(username, loggedUserId);
  }

  @Override
  @Transactional
  public ProfileData unfollow(Long loggedUserId, String username) {
    User loggedUser = usersService.findById(loggedUserId);
    User userToUnfollow = usersService.findByUsername(username);
    UsersFollowers usersFollowers =
        usersFollowersRepository.findByKey(getUsersFollowersKey(loggedUser, userToUnfollow));
    usersFollowersRepository.remove(usersFollowers);
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
