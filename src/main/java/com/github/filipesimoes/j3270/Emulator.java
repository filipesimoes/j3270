package com.github.filipesimoes.j3270;

import java.io.Closeable;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;

import com.github.filipesimoes.j3270.command.AsciiRCLCommand;
import com.github.filipesimoes.j3270.command.AsciiRCRCCommand;
import com.github.filipesimoes.j3270.command.ConnectCommand;
import com.github.filipesimoes.j3270.command.ExpectTextCommand;
import com.github.filipesimoes.j3270.command.IsConnectedCommand;
import com.github.filipesimoes.j3270.command.MoveCursorCommand;
import com.github.filipesimoes.j3270.command.SendKeysCommand;
import com.github.filipesimoes.j3270.command.SendStringCommand;
import com.github.filipesimoes.j3270.command.WaitCommand;

public class Emulator implements Closeable, AutoCloseable {

  private ExecutorService executorService;
  private boolean ownsExecutor = false;

  private Emulator3270Runner runner;
  private TerminalCommander commander;

  public Emulator() {
    this(3270);
  }

  public Emulator(int scriptPort) {
    this(scriptPort, Executors.newFixedThreadPool(1));
    ownsExecutor = true;
  }

  public Emulator(ExecutorService executorService) {
    this(3270, executorService);
  }

  public Emulator(int scriptPort, ExecutorService executorService) {
    super();
    this.executorService = executorService;
    this.runner = new Emulator3270Runner(scriptPort);
    this.commander = new TerminalCommander(scriptPort);
  }

  public void start() throws UnknownHostException, IOException, TimeoutException {
    this.executorService.submit(runner);

    try {
      waitForEmulator();
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
    this.commander.connect();
  }

  private void waitForEmulator() throws InterruptedException, TimeoutException {
    int attempts = 0;
    while (!runner.isStarted()) {
      Thread.sleep(50);
      attempts++;

      if (attempts > 10) {
        throw new TimeoutException("Emulator start timed out.");
      }
    }
  }

  @Override
  public void close() {
    if (runner != null) {
      runner.stop();
    }
    if (commander != null) {
      try {
        commander.close();
      } catch (IOException e) {
        // Nothing to do.
      }
    }
    if (ownsExecutor) {
      executorService.shutdown();
    }
  }

  public boolean isNvt() {
    return this.runner.isNvt();
  }

  public void setNvt(boolean nvt) {
    this.runner.setNvt(nvt);
  }

  public boolean isVisible() {
    return this.runner.isVisible();
  }

  public void setVisible(boolean visible) {
    this.runner.setVisible(visible);
  }

  public String getModel() {
    return this.runner.getModel();
  }

  public void setModel(String model) {
    this.runner.setModel(model);
  }

  public void waitUnlock() {
    waitUnlock(0);
  }

  public boolean waitUnlock(int timeoutInSeconds) {
    return execute(new WaitCommand(timeoutInSeconds, "Unlock"));
  }

  public boolean waitField(int timeoutInSeconds) {
    return execute(new WaitCommand(timeoutInSeconds, "InputField"));
  }

  public void waitField() {
    waitField(0);
  }

  public boolean waitNVTMode(int timeoutInSeconds) {
    return execute(new WaitCommand(timeoutInSeconds, "NVTMode"));
  }

  public void expectText(String text) {
    execute(new ExpectTextCommand(text));
  }

  public boolean expectText(String text, int timeoutInSeconds) {
    return execute(new ExpectTextCommand(text, timeoutInSeconds));
  }

  public void fillField(int row, int col, String txt) {
    execute(new MoveCursorCommand(row, col));
    execute(new SendKeysCommand("DeleteField"));
    execute(new SendStringCommand(txt));
  }

  public String getText(int row, int col, int length) {
    return execute(new AsciiRCLCommand(row, col, length));
  }

  public List<String> getText(int row1, int col1, int row2, int col2) {
    return execute(new AsciiRCRCCommand(row1, col1, row2, col2));
  }

  public String getTextInterval(int row1, int col1, int col2) {
    List<String> result = execute(new AsciiRCRCCommand(row1, col1, row1, col2));
    if (result != null) {
      return result.get(0);
    }
    return null;
  }

  public boolean containsText(int row, int col, String text) {
    String txt = getText(row, col, text.length());
    return text.equals(txt);
  }

  public void sendEnter() {
    execute(new SendKeysCommand("Enter"));
  }

  public boolean connect(String hostname) {
    return execute(new ConnectCommand(hostname));
  }

  public boolean disconnect() {
    execute(new SendKeysCommand("Disconnect"));
    return execute(new WaitCommand("Disconnect"));
  }

  public boolean isConnected() {
    return execute(new IsConnectedCommand());
  }

  protected <V> V execute(Command<V> command) {
    if (this.commander == null) {
      throw new IllegalStateException("Emulator not started.");
    }
    return this.commander.execute(command);
  }

}
