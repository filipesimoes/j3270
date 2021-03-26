package com.github.filipesimoes.j3270;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Emulator3270Runner implements Runnable {

  private int scriptPort;

  private boolean visible = false;
  private boolean nvt = false;
  private String model = "3279-4";

  private Process process = null;
  private boolean started = false;

  public Emulator3270Runner(int scriptPort) {
    super();
    this.scriptPort = scriptPort;
  }

  @Override
  public void run() {
    String executable = getExecutable();

    if (!isExecutablePresent(executable)) {
      throw new RuntimeException(executable + " not found.");
    }

    List<String> args = buildArgs(executable);

    ProcessBuilder pb = new ProcessBuilder(args);
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

  private List<String> buildArgs(String executable) {
    List<String> args = new ArrayList<>();

    args.add(executable);
    args.add("-scriptport");
    args.add("localhost:" + scriptPort);
    args.add("-model");
    args.add(model);

    if (nvt) {
      args.add("-nvt");
    }

    return args;
  }

  private boolean isExecutablePresent(String executable) {
    try {
      ProcessBuilder pb = new ProcessBuilder(isWindows() ? "where" : "which", executable);
      Process proc = pb.start();
      int resultCode = proc.waitFor();
      return resultCode == 0;
    } catch (IOException e) {
      throw new RuntimeException(e);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  private String getExecutable() {
    String executable;
    if (isWindows()) {
      executable = getWindowsExecutable();
    } else {
      executable = getUnixExecutable();
    }
    return executable;
  }

  private String getWindowsExecutable() {
    String executable;
    if (visible) {
      executable = "wc3270";
    } else {
      executable = "ws3270";
    }
    return executable;
  }

  private String getUnixExecutable() {
    String executable;
    if (visible) {
      executable = "x3270";
    } else {
      executable = "s3270";
    }
    return executable;
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

  private boolean isWindows() {
    String osName = System.getProperty("os.name", "generic");
    return osName.toLowerCase().contains("windows");
  }

  public boolean isNvt() {
    return nvt;
  }

  public void setNvt(boolean nvt) {
    this.nvt = nvt;
  }

  public boolean isVisible() {
    return visible;
  }

  public void setVisible(boolean visible) {
    this.visible = visible;
  }

  public String getModel() {
    return model;
  }

  public void setModel(String model) {
    this.model = model;
  }

}
