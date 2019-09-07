package org.example.realworldapi.util;

import org.example.realworldapi.domain.entity.User;
import org.mindrot.jbcrypt.BCrypt;

import java.util.UUID;

public class UserUtils {

    public static User create(String username, String email, String userPassword) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(BCrypt.hashpw(userPassword, BCrypt.gensalt()));
        user.setToken(UUID.randomUUID().toString());
        return user;
    }

}
