# vedirect2mqtt4j
[![MIT License](https://img.shields.io/github/license/hap-java/HAP-Java)](https://github.com/hap-java/HAP-Java/blob/master/LICENSE)

vedirect-2mqtt4j is a java library that transfers data from a [Victron Energy](https://www.victronenergy.com/) device with VE.Direct interface to a MQTT broker.

## Features
The library implements a background service which features:
* reading VE.Direct data from a VE.Direct device connected to a serial port
* VE.Direct protocol frame handler based on the reference implementation in [VE.Direct Protocol FAQ](https://www.victronenergy.com/live/vedirect_protocol:faq).
* publish filter interface to let the application modify the received VE.Direct record and control which record shall be published as MQTT message
* publishes the VE.Direct record as JSON in a MQTT message to a MQTT broker.

## dependencies
| Dependency                                                                  | Description               |
|-----------------------------------------------------------------------------|---------------------------|
| [com.fazecast.jSerialComm](https://github.com/Fazecast/jSerialComm)         | Serial Port Communication |
| [org.eclipse.paho.mqttv5.client](https://github.com/eclipse/paho.mqtt.java) | MQTT v5 client            |
| [com.google.code.gson](https://github.com/google/gson)                      | JSON processing           |


## Usage
Clone this repository, then install it to your local maven:
```
mvn install
```
add dependency in your project
```
<dependency>
    <groupId>info.digitalproject</groupId>
    <artifactId>vedirect-2mqtt4j</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

## Getting Started
The code snippet below is a basic sample of using the vedirect-2mqtt4j library.
```
String portName = "ttyUSB0";
String mqttBrokerUri = "tcp://raspberrypi:1883";
        
// setup options
VeDirect2MqttOptions options = new VeDirect2MqttOptions.Builder(portName, mqttBrokerUri)
        .mqttTopic("Solar/SmartSolarMPPT")
        .build();

// start VeDirect2Mqtt service 
// publishing each VE.Direct record
// as "Solar/SmartSolarMPPT" MQTT topic
VeDirect2Mqtt veDirect2Mqtt = new VeDirect2Mqtt(options);
veDirect2Mqtt.runAsync();
```

Check the [sample](https://github.com/digitalprj/vedirect-2mqtt4j/tree/sample) for more usage of the vedirect-2mqtt4j library.

Create javadoc with:
```
mvn javadoc:jar
```