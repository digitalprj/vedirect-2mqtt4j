package info.digitalproject.vedirect2mqtt4j.sample;

import info.digitalproject.vedirect2mqtt4j.VeDirect2Mqtt;
import info.digitalproject.vedirect2mqtt4j.VeDirect2MqttOptions;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * Demonstrate the usage of VeDirect2MQTT4J with a console application.
 * The application starts the VeDirect2MQTT service and waits until the user enters "exit" to quit.
 * The application takes three arguments:
 * <port name> the serial port name of the connected VE.Direct device
 * <MQTT broker URI> the URI of the MQTT broker syntax: {scheme}://{host}:{port}
 *
 * example usage: java -jar vedirect2mqtt4j-sample-1.0-SNAPSHOT-jar-with-dependencies.jar ttyUSB0 tcp://raspberrypi:1883
 */
public class Main {

    // init a shutdown safe LogManager to log messages during shutdown
    static {
        try (InputStream stream = Main.class.getResourceAsStream("/logging.properties")) {
            LogManager.getLogManager().readConfiguration(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * The logger
     */
    static Logger logger = Logger.getLogger(Main.class.getName());

    /**
     * Main method
     *
     * @param args commandline arguments
     * @throws Exception throw, if error occurs
     */
    public static void main(String[] args) throws Exception {

        // check the arguments...
        if (args.length != 2) {
            // ...not as expected, print usage help
            logger.severe("usage: vedirect2mqtt4j <port name> <MQTT broker URI>");
            System.exit(1);
        }

        logger.info("Start processing... (press Enter key to quit)");

        // get arguments
        String portName = args[0];
        String mqttBrokerUri = args[1];

        // setup options
        VeDirect2MqttOptions options = new VeDirect2MqttOptions.Builder(portName, mqttBrokerUri)
                .mqttTopic("Solar/SmartSolarMPPT")
                .mqttPermanentConnection(false)
                .build();

        // start the VeDirect2Mqtt service
        VeDirect2Mqtt veDirect2Mqtt = new VeDirect2Mqtt(options);
        veDirect2Mqtt.setPublishFilter(new ChangedVoltageFilter());
        veDirect2Mqtt.runAsync();

        // wait for enter key
        try {
            int read = System.in.read();
        } catch (Exception e) {
            logger.severe(e.getMessage());
        }

        veDirect2Mqtt.stop();

        logger.info("Exit vedirect2mqtt4j");
    }
}