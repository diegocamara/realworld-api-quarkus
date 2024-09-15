package org.example.realworldapi.application.web.model.request;

import com.fasterxml.jackson.annotation.JsonRootName;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.example.realworldapi.domain.model.constants.ValidationMessages;
import org.example.realworldapi.domain.model.user.UpdateUserInput;
import org.example.realworldapi.infrastructure.web.validation.constraint.AtLeastOneFieldMustBeNotNull;

@Getter
@Setter
@JsonRootName("user")
@AtLeastOneFieldMustBeNotNull
@RegisterForReflection
public class UpdateUserRequest {

  @Pattern(regexp = "\\A(?!\\s*\\Z).+", message = ValidationMessages.USERNAME_MUST_BE_NOT_BLANK)
  private String username;

  private String bio;
  private String image;
  @Email private String email;

  public UpdateUserInput toUpdateUserInput(UUID userId) {
    return new UpdateUserInput(userId, this.username, this.bio, this.image, this.email);
  }
}
