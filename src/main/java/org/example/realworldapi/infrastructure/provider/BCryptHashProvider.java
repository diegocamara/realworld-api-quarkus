package org.example.realworldapi.infrastructure.provider;

import org.example.realworldapi.domain.model.provider.HashProvider;
import org.mindrot.jbcrypt.BCrypt;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class BCryptHashProvider implements HashProvider {

  @Override
  public String hashPassword(String password) {
    return BCrypt.hashpw(password, BCrypt.gensalt());
  }

  @Override
  public boolean checkPassword(String plaintext, String hashed) {
    return BCrypt.checkpw(plaintext, hashed);
  }
}
