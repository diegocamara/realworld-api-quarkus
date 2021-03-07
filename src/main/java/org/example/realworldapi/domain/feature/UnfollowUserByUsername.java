package org.example.realworldapi.domain.feature;

import java.util.UUID;

public interface UnfollowUserByUsername {
  void handle(UUID loggedUserId, String username);
}
