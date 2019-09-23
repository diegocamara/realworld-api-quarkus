package org.example.realworldapi.web.dto;

import com.fasterxml.jackson.annotation.JsonRootName;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Getter;
import lombok.Setter;
import org.example.realworldapi.domain.entity.persistent.User;
import org.example.realworldapi.web.validation.constraint.AtLeastOneFieldMustBeNotNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@JsonRootName("user")
@AtLeastOneFieldMustBeNotNull
@RegisterForReflection
public class UpdateUserDTO {

  @Pattern(regexp = "\\A(?!\\s*\\Z).+", message = "username must be not blank")
  private String username;

  private String bio;
  private String image;
  @Email private String email;

  public User toUser(Long id) {
    User user = new User();
    user.setId(id);
    user.setUsername(this.username);
    user.setBio(this.bio);
    user.setImage(this.image);
    user.setEmail(this.email);
    return user;
  }
}
