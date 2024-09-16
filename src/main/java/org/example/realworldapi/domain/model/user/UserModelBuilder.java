package org.example.realworldapi.domain.model.user;

import jakarta.inject.Named;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.example.realworldapi.domain.validator.ModelValidator;

@Named
@AllArgsConstructor
public class UserModelBuilder {
  private final ModelValidator modelValidator;

  public User build(String username, String email, String password) {
    return modelValidator.validate(
        new User(UUID.randomUUID(), username, email, password, null, null));
  }

  public User build(
      UUID id, String username, String bio, String image, String password, String email) {
    return modelValidator.validate(new User(id, username, email, password, bio, image));
  }
}
