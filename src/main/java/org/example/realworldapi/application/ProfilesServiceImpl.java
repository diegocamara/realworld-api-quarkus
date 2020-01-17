package org.example.realworldapi.application;

import org.example.realworldapi.application.data.ProfileData;
import org.example.realworldapi.domain.model.builder.ProfileBuilder;
import org.example.realworldapi.domain.model.entity.User;
import org.example.realworldapi.domain.model.entity.UsersFollowed;
import org.example.realworldapi.domain.model.entity.UsersFollowedKey;
import org.example.realworldapi.domain.model.repository.UsersFollowedRepository;
import org.example.realworldapi.domain.service.ProfilesService;
import org.example.realworldapi.domain.service.UsersService;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

@ApplicationScoped
public class ProfilesServiceImpl implements ProfilesService {

  private UsersService usersService;
  private UsersFollowedRepository usersFollowedRepository;

  public ProfilesServiceImpl(
      UsersService usersService, UsersFollowedRepository usersFollowedRepository) {
    this.usersService = usersService;
    this.usersFollowedRepository = usersFollowedRepository;
  }

  @Override
  @Transactional
  public ProfileData getProfile(String username, Long loggedUserId) {
    User existentUser = usersService.findByUsername(username);

    ProfileBuilder profileBuilder = new ProfileBuilder().fromUser(existentUser);

    if (loggedUserId != null) {
      profileBuilder.following(
          usersFollowedRepository.isFollowing(loggedUserId, existentUser.getId()));
    }

    return profileBuilder.build();
  }

  @Override
  @Transactional
  public ProfileData follow(Long loggedUserId, String username) {
    User loggedUser = usersService.findById(loggedUserId);
    User userToFollow = usersService.findByUsername(username);
    usersFollowedRepository.create(getUsersFollowed(loggedUser, userToFollow));
    return getProfile(username, loggedUserId);
  }

  @Override
  @Transactional
  public ProfileData unfollow(Long loggedUserId, String username) {
    User loggedUser = usersService.findById(loggedUserId);
    User userToUnfollow = usersService.findByUsername(username);
    UsersFollowed usersFollowed =
        usersFollowedRepository.findByKey(getUsersFollowedKey(loggedUser, userToUnfollow));
    usersFollowedRepository.remove(usersFollowed);
    return getProfile(username, loggedUserId);
  }

  private UsersFollowed getUsersFollowed(User user, User followed) {
    UsersFollowedKey primaryKey = getUsersFollowedKey(user, followed);
    UsersFollowed usersFollowed = new UsersFollowed();
    usersFollowed.setPrimaryKey(primaryKey);
    return usersFollowed;
  }

  private UsersFollowedKey getUsersFollowedKey(User user, User followed) {
    UsersFollowedKey primaryKey = new UsersFollowedKey();
    primaryKey.setUser(user);
    primaryKey.setFollowed(followed);
    return primaryKey;
  }
}
