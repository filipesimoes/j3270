package com.github.filipesimoes.j3270.command;

public class WaitCommand extends AbstractCommand<Boolean> {

  private boolean success = false;
  private String what;
  private int timeout;

  public WaitCommand(int timeout, String what) {
    super();
    this.what = what;
    this.timeout = timeout;
  }

  public WaitCommand(String what) {
    super();
    this.what = what;
    this.timeout = 0;
  }

  @Override
  protected Boolean getOutput() {
    return success;
  }

  @Override
  protected String getCommand() {
    if (timeout > 0) {
      return "Wait(" + timeout + "," + what + ")";
    } else {
      return "Wait(" + what + ")";
    }
  }

  @Override
  protected void processResult(String result) {
    success = result.equals("ok");
    super.processResult(result);
  }

}
