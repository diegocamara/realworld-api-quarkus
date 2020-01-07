package org.example.realworldapi.domain.model.exception;

public class TagNotFoundException extends BusinessException {

  public TagNotFoundException() {
    super("tag not found");
  }
}
