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
Emulator emulator = new Emulator();
emulator.connect();

emulator.connect('3270host.example.com');
emulator.waitField(10);

emulator.fillField(17, 23, 'mylogin', 8);
emulator.fillField(18, 23, 'mypass', 8);
emulator.sendEnter();

emulator.waitField(10);
emulator.disconnect();
```
