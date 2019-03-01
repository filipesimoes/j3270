package org.j3270.command;

public class AsciiRCLCommand extends AbstractCommand<String> {

  private int row;
  private int col;
  private int length;

  private String ascii = null;

  public AsciiRCLCommand(int row, int col, int length) {
    this.row = row;
    this.col = col;
    this.length = length;
  }

  @Override
  protected void processData(String data) {
    ascii = data;
  }

  @Override
  protected String getOutput() {
    return ascii;
  }

  @Override
  protected String getCommand() {
    return "Ascii(" + row + "," + col + "," + length + ")";
  }

}
