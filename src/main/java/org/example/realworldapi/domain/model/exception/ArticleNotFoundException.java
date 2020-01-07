package org.example.realworldapi.domain.model.exception;

public class ArticleNotFoundException extends BusinessException {

  public ArticleNotFoundException() {
    super("article not found");
  }
}
