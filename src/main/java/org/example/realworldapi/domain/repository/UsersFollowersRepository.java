package org.example.realworldapi.domain.repository;

import org.example.realworldapi.domain.entity.UsersFollowers;
import org.example.realworldapi.domain.entity.UsersFollowersKey;

public interface UsersFollowersRepository {

  boolean isFollowing(Long currentUserId, Long followerUserId);

  UsersFollowers findByKey(UsersFollowersKey primaryKey);

  UsersFollowers insertOrOpdate(UsersFollowers usersFollowers);

  void delete(UsersFollowers usersFollowers);
}
