package org.example.realworldapi.domain.resource.service;

import org.example.realworldapi.domain.entity.Profile;

public interface ProfilesService {
  Profile getProfile(String username, Long loggedUserId);

  Profile follow(Long loggedUserId, String username);

  Profile unfollow(Long loggedUserId, String username);
}
