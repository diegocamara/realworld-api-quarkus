package org.example.realworldapi.domain.feature;

import org.example.realworldapi.domain.model.user.User;

import java.util.UUID;

public interface FindUserById {
  User handle(UUID id);
}
