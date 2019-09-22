package org.example.realworldapi.domain.service.impl;

import org.example.realworldapi.domain.entity.User;
import org.example.realworldapi.domain.entity.UsersFollowers;
import org.example.realworldapi.domain.entity.UsersFollowersKey;
import org.example.realworldapi.domain.repository.UsersFollowersRepository;
import org.example.realworldapi.domain.service.UsersFollowersService;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

@ApplicationScoped
public class UsersFollowersServiceImpl implements UsersFollowersService {

  private UsersFollowersRepository usersFollowersRepository;

  public UsersFollowersServiceImpl(UsersFollowersRepository usersFollowersRepository) {
    this.usersFollowersRepository = usersFollowersRepository;
  }

  @Override
  @Transactional
  public boolean isFollowing(Long currentUserId, Long followerUserId) {
    return usersFollowersRepository.isFollowing(currentUserId, followerUserId);
  }

  @Override
  @Transactional
  public UsersFollowers insertOrUpdate(User user, User follower) {

    UsersFollowers usersFollowers = getUsersFollowers(user, follower);

    return usersFollowersRepository.insertOrOpdate(usersFollowers);
  }

  @Override
  @Transactional
  public void delete(User user, User follower) {
    UsersFollowers usersFollowers =
        usersFollowersRepository.findByKey(getUsersFollowersKey(user, follower));
    usersFollowersRepository.delete(usersFollowers);
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
