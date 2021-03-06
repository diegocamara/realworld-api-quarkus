package org.example.realworldapi.domain.model.exception;

import java.util.LinkedList;
import java.util.List;

import static java.util.Collections.singletonList;

public class BusinessException extends RuntimeException {

  private final List<String> messages;

  public BusinessException() {
    this.messages = new LinkedList<>();
  }

  public BusinessException(String message) {
    super(message);
    this.messages = new LinkedList<>(singletonList(message));
  }

  public BusinessException(List<String> messages) {
    super(String.join(", ", messages));
    this.messages = messages;
  }

  public List<String> messages() {
    return this.messages;
  }
}
