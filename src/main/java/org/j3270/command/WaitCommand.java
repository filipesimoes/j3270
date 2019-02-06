package org.j3270.command;

public class WaitCommand extends AbstractCommand<Void> {

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
  protected Void getOutput() {
    return null;
  }

  @Override
  protected String getCommand() {
    if (timeout > 0) {
      return "Wait(" + timeout + "," + what + ")";
    } else {
      return "Wait(" + what + ")";
    }
  }

}
