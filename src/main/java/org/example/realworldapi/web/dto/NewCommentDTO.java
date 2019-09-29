package org.example.realworldapi.web.dto;

import com.fasterxml.jackson.annotation.JsonRootName;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@JsonRootName("comment")
@RegisterForReflection
public class NewCommentDTO {
  @NotBlank(message = "body must be not blank")
  private String body;
}
