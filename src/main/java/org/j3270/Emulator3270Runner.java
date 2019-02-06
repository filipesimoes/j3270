package org.j3270;

import java.io.IOException;

public class Emulator3270Runner implements Runnable {

  private boolean visible = false;
  private int scriptPort;

  private Process process = null;
  private boolean started = false;

  public Emulator3270Runner(boolean visible, int scriptPort) {
    super();
    this.visible = visible;
    this.scriptPort = scriptPort;
  }

  public Emulator3270Runner(int scriptPort) {
    super();
    this.scriptPort = scriptPort;
  }

  @Override
  public void run() {
    String executable;
    if (visible) {
      executable = "x3270";
    } else {
      executable = "s3270";
    }
    ProcessBuilder pb = new ProcessBuilder(executable, "-scriptport", "localhost:" + Integer.toString(scriptPort));
    try {
      process = pb.start();
      started = true;
      process.waitFor();
    } catch (IOException e) {
      throw new RuntimeException(e);
    } catch (InterruptedException e) {
      // Nothing to do.
    } finally {
      stopNow();
    }

  }

  public void stopNow() {
    if (process != null && process.isAlive()) {
      process.destroyForcibly();
    }
  }

  public void stop() {
    if (process != null && process.isAlive()) {
      process.destroy();
    }
  }

  public boolean isStarted() {
    return started;
  }

}
