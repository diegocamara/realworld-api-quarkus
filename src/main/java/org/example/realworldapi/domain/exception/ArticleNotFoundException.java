package org.example.realworldapi.domain.exception;

public class ArticleNotFoundException extends BusinessException {

  public ArticleNotFoundException() {
    super("article not found");
  }
}
