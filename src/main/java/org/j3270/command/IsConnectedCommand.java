package org.j3270.command;

public class IsConnectedCommand extends AbstractCommand<Boolean> {

  private boolean connected = false;

  public IsConnectedCommand() {
    super();
  }

  @Override
  protected String getCommand() {
    return "Wait(0, Seconds)";
  }

  @Override
  protected void processStatus(String status) {
    String[] fields = status.split(" ");
    connected = fields.length >= 4 && fields[3].startsWith("C");
  }

  @Override
  protected Boolean getOutput() {
    return connected;
  }

}
