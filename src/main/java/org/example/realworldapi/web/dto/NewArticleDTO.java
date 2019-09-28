package org.example.realworldapi.web.dto;

import com.fasterxml.jackson.annotation.JsonRootName;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
@JsonRootName("article")
@RegisterForReflection
public class NewArticleDTO {

  @NotBlank(message = "title must not be blank")
  private String title;

  @NotBlank(message = "description must not be blank")
  private String description;

  @NotBlank(message = "body must not be blank")
  private String body;

  private List<String> tagList;
}
