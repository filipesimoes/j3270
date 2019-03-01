package org.j3270.command;

import java.util.ArrayList;
import java.util.List;

public class AsciiRCRCCommand extends AbstractCommand<List<String>> {

  private int row1;
  private int col1;
  private int row2;
  private int col2;

  private List<String> ascii = null;

  public AsciiRCRCCommand(int row1, int col1, int row2, int col2) {
    this.row1 = row1;
    this.col1 = col1;
    this.row2 = row2;
    this.col2 = col2;
  }

  @Override
  protected void processData(String data) {
    if (ascii == null) {
      ascii = new ArrayList<>();
    }

    ascii.add(data);
  }

  @Override
  protected List<String> getOutput() {
    return ascii;
  }

  @Override
  protected String getCommand() {
    return "Ascii(" + row1 + "," + col1 + "," + (row2 - row1) + "," + (col2 - col1) + ")";
  }

}
