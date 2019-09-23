package org.example.realworldapi.web.dto;

import com.fasterxml.jackson.annotation.JsonRootName;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.realworldapi.domain.entity.persistent.User;

@Getter
@Setter
@NoArgsConstructor
@JsonRootName("user")
@RegisterForReflection
public class UserDTO {

  private String username;
  private String bio;
  private String image;
  private String email;
  private String token;

  public UserDTO(User user) {
    this.username = user.getUsername();
    this.bio = user.getBio();
    this.image = user.getImage();
    this.email = user.getEmail();
    this.token = user.getToken();
  }
}
