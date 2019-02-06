package org.j3270.command;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Writer;

import org.j3270.Command;

public abstract class AbstractCommand<V> implements Command<V> {

  private static final String dataPrefix = "data:";

  @Override
  public V execute(Writer writer, BufferedReader reader) {
    try {
      writer.write(getCommand());
      writer.write("\n");
      writer.flush();
      return processResult(reader);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private V processResult(BufferedReader reader) throws IOException {

    boolean finished = false;
    boolean statusFound = false;

    while (!finished) {
      String line = reader.readLine();

      if (line.startsWith(dataPrefix)) {
        processData(line);
      } else if (!statusFound) {
        processStatus(line);
        statusFound = true;
      } else {
        processResult(line);
        finished = true;
      }

    }

    return getOutput();
  }

  protected void processData(String data) {
    // optional implementation.
  }

  protected void processStatus(String status) {
    // optional implementation.
  }

  protected void processResult(String result) {
    // optional implementation.
  }

  protected abstract V getOutput();

  protected abstract String getCommand();

}
