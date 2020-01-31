package org.example.realworldapi.domain.model.repository;

import org.example.realworldapi.domain.model.entity.UsersFollowed;
import org.example.realworldapi.domain.model.entity.UsersFollowedKey;

public interface UsersFollowedRepository {

  boolean isFollowing(Long currentUserId, Long followedUserId);

  UsersFollowed findByKey(UsersFollowedKey primaryKey);

  UsersFollowed create(UsersFollowed usersFollowed);

  void remove(UsersFollowed usersFollowed);
}
