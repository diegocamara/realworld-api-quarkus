package org.example.realworldapi.domain.exception;

public class CommentNotFoundException extends BusinessException {
  public CommentNotFoundException() {
    super(6, "comment not found");
  }
}
