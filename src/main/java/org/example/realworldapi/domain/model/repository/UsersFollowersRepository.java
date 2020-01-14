package org.example.realworldapi.domain.model.repository;

import org.example.realworldapi.domain.model.entity.UsersFollowers;
import org.example.realworldapi.domain.model.entity.UsersFollowersKey;

public interface UsersFollowersRepository {

  boolean isFollowing(Long currentUserId, Long followerUserId);

  UsersFollowers findByKey(UsersFollowersKey primaryKey);

  UsersFollowers create(UsersFollowers usersFollowers);

  void remove(UsersFollowers usersFollowers);
}
