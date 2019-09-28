package org.example.realworldapi.web.dto;

import com.fasterxml.jackson.annotation.JsonRootName;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.realworldapi.web.validation.constraint.AtLeastOneFieldMustBeNotNull;

@Getter
@Setter
@NoArgsConstructor
@JsonRootName("article")
@AtLeastOneFieldMustBeNotNull
@RegisterForReflection
public class UpdateArticleDTO {

  private String title;

  private String description;

  private String body;
}
