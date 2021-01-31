package com.github.mmccann94.simplewebcrawler.exception;

public class ResultOutputException extends RuntimeException {

  public ResultOutputException(String message) {
    super(message);
  }

  public ResultOutputException(String message, Throwable cause) {
    super(message, cause);
  }
}
