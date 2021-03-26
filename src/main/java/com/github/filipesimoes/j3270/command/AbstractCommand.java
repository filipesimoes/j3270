package com.github.filipesimoes.j3270.command;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.github.filipesimoes.j3270.Command;

public abstract class AbstractCommand<V> implements Command<V> {

  private static final Logger LOGGER = Logger.getLogger(AbstractCommand.class.getSimpleName());

  private static final String DATA_PREFIX = " data:";

  private String lastData;

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

      LOGGER.log(Level.FINE, line);

      if (line.startsWith(DATA_PREFIX)) {
        line = line.replaceFirst(DATA_PREFIX, "");
        this.lastData = line;
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
  }

  protected void processStatus(String status) {
    // optional implementation.
  }

  protected void processResult(String result) {
    if (result.equals("error")) {
      throw new CommandException(lastData);
    }
  }

  protected abstract V getOutput();

  protected abstract String getCommand();

}
