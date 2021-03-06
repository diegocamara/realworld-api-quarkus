package org.example.realworldapi.domain.feature;

import org.example.realworldapi.domain.model.user.LoginUserInput;
import org.example.realworldapi.domain.model.user.User;

public interface LoginUser {
  User handle(LoginUserInput loginUserInput);
}
