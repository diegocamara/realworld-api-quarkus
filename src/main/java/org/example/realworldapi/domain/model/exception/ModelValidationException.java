package org.example.realworldapi.domain.model.exception;

import java.util.List;

public class ModelValidationException extends BusinessException {

  public ModelValidationException(List<String> messages) {
    super(messages);
  }
}
