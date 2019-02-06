package org.j3270;

import java.io.BufferedReader;
import java.io.Writer;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class CommandTask<V> implements Future<V> {

  private Command<V> command;

  private boolean cancelled = false;
  private boolean done = false;

  private V result = null;
  private Throwable executionEx = null;

  private Object mutex = new Object();

  public CommandTask(Command<V> command) {
    super();
    this.command = command;
  }

  public void execute(Writer writer, BufferedReader reader) {
    if (!cancelled) {
      try {
        result = command.execute(writer, reader);
      } catch (Throwable t) {
        this.executionEx = t;
      }
      done = true;
    }
    synchronized (mutex) {
      mutex.notifyAll();
    }
  }

  @Override
  public boolean cancel(boolean mayInterruptIfRunning) {
    if (!done) {
      cancelled = true;
      return true;
    } else {
      return false;
    }
  }

  @Override
  public boolean isCancelled() {
    return cancelled;
  }

  @Override
  public boolean isDone() {
    return done || cancelled;
  }

  @Override
  public V get() throws InterruptedException, ExecutionException {
    synchronized (mutex) {
      mutex.wait();
    }
    if (executionEx != null) {
      throw new ExecutionException(executionEx);
    }
    return result;
  }

  @Override
  public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
    synchronized (mutex) {
      mutex.wait(unit.toMillis(timeout));
    }
    if (!done) {
      throw new TimeoutException();
    }
    if (executionEx != null) {
      throw new ExecutionException(executionEx);
    }
    return result;
  }

}
