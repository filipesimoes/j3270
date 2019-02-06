package org.j3270.command;

public class CommandException extends RuntimeException {

  private static final long serialVersionUID = 9043337863637817230L;

  public CommandException() {
    super();
  }

  public CommandException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  public CommandException(String message, Throwable cause) {
    super(message, cause);
  }

  public CommandException(String message) {
    super(message);
  }

  public CommandException(Throwable cause) {
    super(cause);
  }

}
