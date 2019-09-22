package org.example.realworldapi.domain.service;

import org.example.realworldapi.domain.entity.User;
import org.example.realworldapi.domain.entity.UsersFollowers;

public interface UsersFollowersService {
  boolean isFollowing(Long currentUserId, Long followerUserId);

  UsersFollowers insertOrUpdate(User user, User follower);

  void delete(User user, User follower);
}
