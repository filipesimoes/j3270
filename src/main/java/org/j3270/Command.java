package org.j3270;

import java.io.BufferedReader;
import java.io.Writer;

public interface Command<V> {

  V execute(Writer writer, BufferedReader reader);

}
