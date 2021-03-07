package org.example.realworldapi.domain.model.user;

import java.util.Optional;
import java.util.UUID;

public interface FollowRelationshipRepository {

  boolean isFollowing(UUID currentUserId, UUID followedUserId);

  void save(FollowRelationship followRelationship);

  Optional<FollowRelationship> findByUsers(User loggedUser, User followedUser);

  void remove(FollowRelationship followRelationship);
}
