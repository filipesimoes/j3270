package org.j3270.command;

public class SendKeysCommand extends AbstractCommand<Void> {

  private String keys;

  public SendKeysCommand(String keys) {
    super();
    this.keys = keys;
  }

  @Override
  protected Void getOutput() {
    return null;
  }

  @Override
  protected String getCommand() {
    return keys;
  }

}
