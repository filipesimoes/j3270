package org.j3270.command;

public class MoveCursorCommand extends AbstractCommand<Void> {

  private int row;
  private int col;

  public MoveCursorCommand(int row, int col) {
    super();
    this.row = row;
    this.col = col;
  }

  @Override
  protected Void getOutput() {
    return null;
  }

  @Override
  protected String getCommand() {
    return "MoveCursor(" + row + "," + col + ")";
  }

}
