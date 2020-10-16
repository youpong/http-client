# Command line web client

A simple HTTP 1.1 command line web client implemented in Java.

This software is released under MIT license.


## Run

The web client runs on any OS with Java (JRE)1.8+ installed.

run the following command:

```bash
$ java -jar http-client.jar [-n TIMES] URL [DEST]
```

options:
- `-n TIMES` : request TIMES times for stress test.

## BUILD

Run the following command:

```bash
$ mvn package
```

This will create a "target" folder containing the application jar file:
http-client.jar.
