package org.example.realworldapi.domain.exception;

public class TagNotFoundException extends BusinessException {

  public TagNotFoundException() {
    super(7, "tag not found");
  }
}
