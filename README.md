# j3270 [![Release](https://github.com/filipesimoes/j3270/actions/workflows/release.yml/badge.svg)](https://github.com/filipesimoes/j3270/actions/workflows/release.yml) [![Maven Central](https://img.shields.io/maven-central/v/com.github.filipesimoes/j3270.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22com.github.filipesimoes%22%20AND%20a:%22j3270%22)

A Java Wrapper for x3270 (IBM 3270 terminal emulator) based on [py3270](https://github.com/py3270/py3270).

It is an Java API for x3270 (Linux) or s3270 (Windows) subprocess.
# Compiling

```
git clone git@github.com:filipesimoes/j3270.git
cd j3270
mvn clean install
```

# Usage

It is necessary to have installed and in your path [x3270](http://x3270.bgp.nu/) emulator.
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

If you need more details, read [Emulator](https://github.com/filipesimoes/j3270/blob/4e320a94b5260bcae3be955fb987f7af1c73279f/src/main/java/com/github/filipesimoes/j3270/Emulator.java) class.
