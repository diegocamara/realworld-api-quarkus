package org.example.realworldapi.infrastructure.web.model.request;

import com.fasterxml.jackson.annotation.JsonRootName;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Getter;
import lombok.Setter;
import org.example.realworldapi.domain.model.constants.ValidationMessages;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@JsonRootName("comment")
@RegisterForReflection
public class NewCommentRequest {
  @NotBlank(message = ValidationMessages.BODY_MUST_BE_NOT_BLANK)
  private String body;
}
