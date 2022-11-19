package info.digitalproject.vedirect2mqtt4j;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortInvalidPortException;
import com.google.gson.Gson;
import info.digitalproject.vedirect2mqtt4j.exceptions.MqttClientException;
import info.digitalproject.vedirect2mqtt4j.exceptions.VeDirectInvalidPortException;
import info.digitalproject.vedirect2mqtt4j.mqtt.MqttHandler;
import info.digitalproject.vedirect2mqtt4j.vedirect.VeDirectProtocolListener;
import info.digitalproject.vedirect2mqtt4j.vedirect.VeDirectRecord;
import info.digitalproject.vedirect2mqtt4j.vedirect.VeDirectTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Transfers the data received from a VE.Direct device into the MQTT protocol.
 * Starts a background service which reads VE.Direct data from a VE.Direct device
 * connected to a serial port and publishes the received VE.Direct data to a MQTT broker.
 * The received VE.Direct data is published as JSON in a MQTT message.
 */
public class VeDirect2Mqtt implements VeDirectProtocolListener {

    /**
     * Log4j logger
     */
    private static final Logger logger = Logger.getLogger(VeDirect2Mqtt.class.getName());

    /**
     * MQTT handler
     */
    private final MqttHandler mqttHandler;

    /**
     * VeDirect2Mqtt options holding all the options needed to transfer data between VE.Direct to MQTT
     */
    private final VeDirect2MqttOptions options;

    /**
     * Asynchronous task executor service
     */
    private ScheduledExecutorService executorService;

    /**
     * Json parser
     */
    private final Gson gson;

    /**
     * Record publish filter
     */
    private PublishFilter publishFilter;

    /**
     * Creates a new VeDirect2Mqtt instance
     * @param options all options needed for the data transfer
     * @throws MqttClientException if exception in MQTT client occurs
     */
    public VeDirect2Mqtt(VeDirect2MqttOptions options) throws MqttClientException {
        this.options = options;
        mqttHandler = new MqttHandler(options);
        executorService = Executors.newScheduledThreadPool(2);
        gson = new Gson();
        publishFilter = null;
    }

    /**
     * Run the VE.Direct to MQTT transfer as background service
     * @throws MqttClientException if a exception in MQTT client occurs
     * @throws VeDirectInvalidPortException if the serial port for VE.Direct is invalid
     */
    public void runAsync() throws MqttClientException, VeDirectInvalidPortException {
        logger.info("Start VeDirect2Mqtt");

        // permanet MQTT connection?
        if (options.isMqttPermanentConnection()) {
            mqttHandler.connect();
        }

        // start VE.Direct reading task
        try {
            VeDirectTask veDirectTask = new VeDirectTask(options, this);
            executorService.submit(veDirectTask);
        } catch (SerialPortInvalidPortException spipe) {
            throw new VeDirectInvalidPortException(formatInvalidPortMessage(options.getVeDirectPortName()), spipe);
        }
    }

    /**
     * Stops the VE.Direct to MQTT transfer background service
     * @throws MqttClientException if error occurs when disconnection the MQTT client
     */
    public void stop() throws MqttClientException {
        logger.info("Stop VeDirect2Mqtt");

        // disconnect MQTT client
        mqttHandler.disconnect();

        // cancel all tasks
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(2000, TimeUnit.MILLISECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Called by VeDirectProtocolHandler when a valid record is received from VE.Direct.
     * Publish the record as JSON data in a MQTT message in the publish interval.
     * @param record holding the received VE.Direct field names and values organized as record
     */
    @Override
    public void onRecordReceived(VeDirectRecord record) {

        logger.fine("VE.Direct Record received");

        if (publishFilter != null) {
            record = publishFilter.execute(record);
        }

        if (record != null) {
            try {
                mqttHandler.publish(gson.toJson(record));
            } catch (MqttClientException e) {
                logger.severe(e.getMessage());
            }
        }
    }

    /**
     * Format a invalid VE.Direct port message holding the available com port names.
     * @param vedirectPortName name of the invalid port
     * @return invalid port message with hints
     */
    private String formatInvalidPortMessage(String vedirectPortName) {
        StringBuilder message = new StringBuilder();
        message.append("Invalid VE.Direct port ");
        message.append(vedirectPortName);
        message.append(System.lineSeparator());
        message.append("Valid VE.Direct ports are: ");
        for (SerialPort serialPort : SerialPort.getCommPorts()) {
            message.append(serialPort.getSystemPortName());
            message.append(" ");
        }

        return message.toString();
    }

    /**
     * Set the publish filter
     * @param publishFilter new filter
     */
    public void setPublishFilter(PublishFilter publishFilter) {
        this.publishFilter = publishFilter;
    }
}
