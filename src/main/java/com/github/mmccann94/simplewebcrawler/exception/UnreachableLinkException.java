package com.github.mmccann94.simplewebcrawler.exception;

public class UnreachableLinkException extends RuntimeException {

  public UnreachableLinkException(String message) {
    super(message);
  }

  public UnreachableLinkException(String message, Throwable cause) {
    super(message, cause);
  }
}
