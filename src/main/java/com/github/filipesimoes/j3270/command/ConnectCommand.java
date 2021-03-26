package com.github.filipesimoes.j3270.command;

public class ConnectCommand extends AbstractCommand<Boolean> {

  private String hostname;
  private boolean connected = false;

  public ConnectCommand(String hostname) {
    super();
    this.hostname = hostname;
  }

  @Override
  protected String getCommand() {
    return "Connect(" + hostname + ")";
  }

  @Override
  protected void processResult(String result) {
    connected = result.equals("ok");
  }

  @Override
  protected Boolean getOutput() {
    return connected;
  }

}
