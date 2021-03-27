# j3270
Java Wrapper for x3270 (IBM 3270 terminal emulator) [![Release](https://github.com/filipesimoes/j3270/actions/workflows/release.yml/badge.svg)](https://github.com/filipesimoes/j3270/actions/workflows/release.yml) [![Maven Central](https://img.shields.io/maven-central/v/com.github.filipesimoes/j3270.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22com.github.filipesimoes%22%20AND%20a:%22j3270%22)
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
