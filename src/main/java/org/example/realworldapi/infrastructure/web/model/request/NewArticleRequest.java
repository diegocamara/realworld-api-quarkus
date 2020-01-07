package org.example.realworldapi.infrastructure.web.model.request;

import com.fasterxml.jackson.annotation.JsonRootName;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Getter;
import lombok.Setter;
import org.example.realworldapi.domain.model.constants.ValidationMessages;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
@JsonRootName("article")
@RegisterForReflection
public class NewArticleRequest {

  @NotBlank(message = ValidationMessages.TITLE_MUST_BE_NOT_BLANK)
  private String title;

  @NotBlank(message = ValidationMessages.DESCRIPTION_MUST_BE_NOT_BLANK)
  private String description;

  @NotBlank(message = ValidationMessages.BODY_MUST_BE_NOT_BLANK)
  private String body;

  private List<String> tagList;
}
