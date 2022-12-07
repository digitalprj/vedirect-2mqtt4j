Sample usage of vedirect-2mqtt4j library.

Demonstrate the usage of vedirect-2mqtt4j with a console application.
The application starts the VeDirect2MQTT service and waits until the user press enter key to gracefully stop the service and quit the application.

Build the application with `mvn package` to get a *vedirect-2mqtt4j-sample-1.0-SNAPSHOT.jar* and a *vedirect-2mqtt4j-sample-1.0-SNAPSHOT-jar-with-dependencies.jar*
with all dependencies.

The application takes two arguments:

`<port name>` the serial port name of the connected VE.Direct device

`<MQTT broker URI>` the URI of the MQTT broker syntax: {scheme}://{host}:{port}

example usage: 
```
java -jar vedirect-2mqtt4j-sample-1.0-SNAPSHOT-jar-with-dependencies.jar ttyUSB0 tcp://raspberrypi:1883
```

