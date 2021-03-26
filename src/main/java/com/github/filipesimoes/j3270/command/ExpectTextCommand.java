package com.github.filipesimoes.j3270.command;

public class ExpectTextCommand extends AbstractCommand<Boolean> {

  private boolean found = false;
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
  protected Boolean getOutput() {
    return found;
  }

  @Override
  protected String getCommand() {
    if (timeout > 0) {
      return "Expect(\"" + text + "\"," + timeout + ")";
    } else {
      return "Expect(\"" + text + "\")";
    }

  }

  @Override
  protected void processResult(String result) {
    found = result.equals("ok");
    super.processResult(result);
  }

}
