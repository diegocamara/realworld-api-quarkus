package org.example.realworldapi.domain.service;

import org.example.realworldapi.domain.entity.Profile;

public interface ProfilesService {
  Profile getProfile(String username, Long loggedUserId);
}
