package org.example.realworldapi.util;

import org.example.realworldapi.domain.model.entity.User;
import org.mindrot.jbcrypt.BCrypt;

public class UserUtils {

  public static User create(String username, String email, String userPassword) {
    User user = new User();
    user.setUsername(username);
    user.setEmail(email);
    user.setPassword(BCrypt.hashpw(userPassword, BCrypt.gensalt()));
    return user;
  }

  public static User create(Long id, String username, String email, String userPassword) {
    User user = create(username, email, userPassword);
    user.setId(id);
    return user;
  }

  public static User create(
      String username, String email, String userPassword, String bio, String image) {
    User user = create(username, email, userPassword);
    user.setBio(bio);
    user.setImage(image);
    return user;
  }
}
