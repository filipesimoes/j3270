package org.j3270;

import java.io.Closeable;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;

import org.j3270.command.ConnectCommand;
import org.j3270.command.ExpectTextCommand;
import org.j3270.command.MoveCursorCommand;
import org.j3270.command.SendKeysCommand;
import org.j3270.command.SendStringCommand;
import org.j3270.command.WaitCommand;

public class Emulator implements Closeable, AutoCloseable {

  private int scriptPort;
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

  public Emulator(int scriptPort, ExecutorService executorService) {
    super();
    this.scriptPort = scriptPort;
    this.executorService = executorService;
  }

  public Emulator(ExecutorService executorService) {
    this(3270, executorService);
  }

  public void start() throws UnknownHostException, IOException, TimeoutException {
    start(false);
  }

  public void start(boolean visible) throws UnknownHostException, IOException, TimeoutException {
    this.runner = new Emulator3270Runner(visible, scriptPort);
    this.executorService.submit(runner);

    try {
      waitForEmulator();
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }

    this.commander = new TerminalCommander(scriptPort);
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

  public void waitUnlock() {
    waitUnlock(0);
  }

  public void waitUnlock(int timeoutInSeconds) {
    execute(new WaitCommand(timeoutInSeconds, "Unlock"));
  }

  public void waitField(int timeoutInSeconds) {
    execute(new WaitCommand(timeoutInSeconds, "InputField"));
  }

  public void waitField() {
    waitField(0);
  }

  public void expectText(String text, int timeoutInSeconds) {
    execute(new ExpectTextCommand(text, timeoutInSeconds));
  }

  public void fillField(int row, int col, String txt) {
    execute(new MoveCursorCommand(row, col));
    execute(new SendKeysCommand("DeleteField"));
    execute(new SendStringCommand(txt));
  }

  public void sendEnter() {
    execute(new SendKeysCommand("Enter"));
  }

  public boolean connect(String hostname) {
    return execute(new ConnectCommand(hostname));
  }

  public void disconnect() {
    execute(new SendKeysCommand("Disconnect"));
    execute(new SendKeysCommand("Wait(Disconnect)"));
  }

  private <V> V execute(Command<V> command) {
    if (this.commander == null) {
      throw new IllegalStateException("Emulator not started.");
    }
    return this.commander.execute(command);
  }

}
