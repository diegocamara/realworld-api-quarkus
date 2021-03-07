package org.example.realworldapi.domain.model.tag;

import lombok.AllArgsConstructor;
import org.example.realworldapi.domain.validator.ModelValidator;

import java.util.UUID;

@AllArgsConstructor
public class TagBuilder {
  private final ModelValidator modelValidator;

  public Tag build(String name) {
    return modelValidator.validate(new Tag(UUID.randomUUID(), name));
  }

  public Tag build(UUID id, String name) {
    return modelValidator.validate(new Tag(id, name));
  }
}
