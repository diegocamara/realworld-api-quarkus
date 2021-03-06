package org.example.realworldapi.domain.feature;

import org.example.realworldapi.domain.model.user.UpdateUserInput;
import org.example.realworldapi.domain.model.user.User;

public interface UpdateUser {
  User handle(UpdateUserInput updateUserInput);
}
