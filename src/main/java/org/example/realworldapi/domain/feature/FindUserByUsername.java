package org.example.realworldapi.domain.feature;

import org.example.realworldapi.domain.model.user.User;

public interface FindUserByUsername {
  User handle(String username);
}
