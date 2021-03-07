package org.example.realworldapi.domain.feature;

import java.util.UUID;

public interface IsFollowingUser {
  boolean handle(UUID currentUserId, UUID followedUserId);
}
