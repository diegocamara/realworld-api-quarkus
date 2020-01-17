package org.example.realworldapi.domain.service;

import org.example.realworldapi.application.data.ProfileData;

public interface ProfilesService {
  ProfileData getProfile(String username, Long loggedUserId);

  ProfileData follow(Long loggedUserId, String username);

  ProfileData unfollow(Long loggedUserId, String username);
}
