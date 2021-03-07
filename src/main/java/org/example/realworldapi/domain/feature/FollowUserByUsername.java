package org.example.realworldapi.domain.feature;

import org.example.realworldapi.domain.model.user.FollowRelationship;

import java.util.UUID;

public interface FollowUserByUsername {
  FollowRelationship handle(UUID loggedUserId, String username);
}
