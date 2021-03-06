package org.example.realworldapi.domain.feature;

import org.example.realworldapi.domain.model.user.CreateUserInput;
import org.example.realworldapi.domain.model.user.User;

public interface CreateUser {
  User handle(CreateUserInput createUserInput);
}
