package org.j3270.command;

public class ExpectTextCommand extends AbstractCommand<Void> {

  private String text;
  private int timeout;

  public ExpectTextCommand(String text, int timeout) {
    super();
    this.text = text;
    this.timeout = timeout;
  }

  public ExpectTextCommand(String text) {
    super();
    this.text = text;
    this.timeout = 0;
  }

  @Override
  protected Void getOutput() {
    return null;
  }

  @Override
  protected String getCommand() {
    if (timeout > 0) {
      return "Expect(" + text + "," + timeout + ")";
    } else {
      return "Expect(" + text + ")";
    }

  }

}
