package org.example.realworldapi.application.web.model.request;

import com.fasterxml.jackson.annotation.JsonRootName;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Getter;
import lombok.Setter;
import org.example.realworldapi.domain.model.user.CreateUserInput;

@Getter
@Setter
@JsonRootName("user")
@RegisterForReflection
public class NewUserRequest {
  private String username;
  private String email;
  private String password;

  public CreateUserInput toCreateUserInput() {
    return new CreateUserInput(this.username, this.email, this.password);
  }
}
