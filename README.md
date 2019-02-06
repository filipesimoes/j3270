# j3270
Java Wrapper for x3270 (IBM 3270 terminal emulator)

# Compiling

```
git clone git@github.com:filipesimoes/j3270.git
cd j3270
mvn clean install
```

# Usage
```
try (Emulator emulator = new Emulator()) {
  emulator.start();
  emulator.connect("3270host.example.com");
  emulator.waitField(10);

  emulator.fillField(17, 23, "mylogin");
  emulator.fillField(18, 23, "mypass");
  emulator.sendEnter();

  emulator.waitField(10);
  emulator.disconnect();
}
```
