package org.example.realworldapi.application.web.model.request;

import com.fasterxml.jackson.annotation.JsonRootName;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.validation.constraints.NotBlank;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.example.realworldapi.domain.model.comment.NewCommentInput;
import org.example.realworldapi.domain.model.constants.ValidationMessages;

@Getter
@Setter
@JsonRootName("comment")
@RegisterForReflection
public class NewCommentRequest {
  @NotBlank(message = ValidationMessages.BODY_MUST_BE_NOT_BLANK)
  private String body;

  public NewCommentInput toNewCommentInput(UUID authorId, String articleSlug) {
    return new NewCommentInput(authorId, articleSlug, this.body);
  }
}
