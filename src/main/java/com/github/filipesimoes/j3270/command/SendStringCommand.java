package com.github.filipesimoes.j3270.command;

public class SendStringCommand extends AbstractCommand<Void> {

  private String string;

  public SendStringCommand(String string) {
    super();
    this.string = string;
  }

  @Override
  protected Void getOutput() {
    return null;
  }

  @Override
  protected String getCommand() {
    return "String(\"" + string + "\")";
  }

}
