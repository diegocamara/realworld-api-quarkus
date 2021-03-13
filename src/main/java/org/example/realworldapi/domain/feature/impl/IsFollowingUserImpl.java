package org.example.realworldapi.domain.feature.impl;

import lombok.AllArgsConstructor;
import org.example.realworldapi.domain.feature.IsFollowingUser;
import org.example.realworldapi.domain.model.user.FollowRelationshipRepository;

import java.util.UUID;

@AllArgsConstructor
public class IsFollowingUserImpl implements IsFollowingUser {

  private final FollowRelationshipRepository usersFollowedRepository;

  @Override
  public boolean handle(UUID currentUserId, UUID followedUserId) {
    return usersFollowedRepository.isFollowing(currentUserId, followedUserId);
  }
}
