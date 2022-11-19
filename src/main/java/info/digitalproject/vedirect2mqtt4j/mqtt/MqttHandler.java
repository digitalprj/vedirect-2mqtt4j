package info.digitalproject.vedirect2mqtt4j.mqtt;

import info.digitalproject.vedirect2mqtt4j.VeDirect2MqttOptions;
import info.digitalproject.vedirect2mqtt4j.exceptions.MqttClientException;
import org.eclipse.paho.mqttv5.client.*;
import org.eclipse.paho.mqttv5.client.persist.MemoryPersistence;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.eclipse.paho.mqttv5.common.packet.MqttProperties;

import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

/**
 * Handles all the MQTT communication logic with the MQTT broker.
 */
public class MqttHandler implements MqttCallback {
    /**
     * Log4j logger
     */
    private static final Logger logger = Logger.getLogger(MqttHandler.class.getName());

    /**
     * Connection options from MQTT client
     */
    private final VeDirect2MqttOptions options;

    /**
     * Holds the set of options that control how the MQTT client connects to a server.
     */
    private MqttConnectionOptions mqttConnectionOptions;

    /**
     * Lightweight client for talking to an MQTT server using non-blocking methods
     * that allow an operation to run in the background.
     */
    private MqttAsyncClient mqttAsyncClient;

    /**
     * Stores all the MQTT client connection options in memory
     */
    private MemoryPersistence persistence = new MemoryPersistence();

    /**
     * Creates a new instance of the MQTT handler
     * @param options options to configure the MQTT client
     * @throws MqttClientException if error during creation of MQTT client occurs
     */
    public MqttHandler(VeDirect2MqttOptions options) throws MqttClientException {
        this.options = options;

        // Create the client.
        try {
            mqttAsyncClient = new MqttAsyncClient(options.getMqttBrokerUri(), options.getMqttClientId(), persistence);
        } catch (MqttException e) {
            throw new MqttClientException("Error creating MQTT client", e);
        }
        mqttAsyncClient.setCallback(this);

        mqttConnectionOptions = new MqttConnectionOptionsBuilder()
                .cleanStart(true)
                .automaticReconnect(true)
                .build();
    }

    /**
     * Connect to MQTT broker
     * @throws MqttClientException if error connecting to MQTT client occurs
     */
    public void connect() throws MqttClientException {
        try {
            IMqttToken token = mqttAsyncClient.connect(mqttConnectionOptions);
            token.waitForCompletion();
            logger.fine("Connected MQTT broker " + options.getMqttBrokerUri());
        } catch (MqttException e) {
            throw new MqttClientException("Error connecting MQTT client", e);
        }
    }

    /**
     * Disconnect to MQTT broker
     * @throws MqttClientException if error disconnecting from MQTT client occurs
     */
    public void disconnect() throws MqttClientException {
        if (mqttAsyncClient.isConnected()) {
            try {
                IMqttToken token = mqttAsyncClient.disconnect();
                token.waitForCompletion();
                logger.fine("Disconnected MQTT broker " + options.getMqttBrokerUri());
            } catch (MqttException e) {
                throw new MqttClientException("Error disconnecting MQTT client", e);
            }
        }
    }

    /**
     * Publishes a MQTT message
     * @param content message data
     * @throws MqttClientException if message can not be published
     */
    public void publish(String content) throws MqttClientException {
        logger.fine("Publish message");

        if (!options.isMqttPermanentConnection()) {
            connect();
        }

        try {
            MqttMessage v5Message = new MqttMessage(content.getBytes(StandardCharsets.UTF_8));
            v5Message.setQos(options.getMqttQos());
            v5Message.setRetained(options.isMqttRetained());
            IMqttToken deliveryToken = mqttAsyncClient.publish(options.getMqttTopic(), v5Message);
            deliveryToken.waitForCompletion();
        } catch (MqttException e) {
            throw new MqttClientException("Error publishing MQTT message", e);
        }

        if (!options.isMqttPermanentConnection()) {
            disconnect();
        }
    }

    /**
     * called when the server gracefully disconnects from the client
     * @param disconnectResponse cause of the disconnection
     */
    @Override
    public void disconnected(MqttDisconnectResponse disconnectResponse) {
        String cause;
        if (disconnectResponse.getException().getMessage() != null) {
            cause = disconnectResponse.getException().getMessage();
        } else {
            cause = disconnectResponse.getReasonString();
        }
        if (mqttConnectionOptions.isAutomaticReconnect()) {
            logger.info(String.format("The connection to the server was lost, cause: %s. Waiting to reconnect.", cause));
        } else {
            logger.info(String.format("The connection to the server was lost, cause: %s. Closing Client", cause));
            try {
                mqttAsyncClient.close();
            } catch (MqttException e) {
                logger.severe(e.getMessage());
            }
            logger.info("Client Closed.");
        }
    }

    /**
     * called when an exception is thrown within the MQTT client
     * @param exception the exception thrown causing the error
     */
    @Override
    public void mqttErrorOccurred(MqttException exception) {
        logger.severe(String.format("An MQTT error occurred: %s", exception.getMessage()));
    }

    /**
     * called when a message arrives from the server
     * @param topic name of the topic on the message was published to
     * @param message the actual message
     */
    @Override
    public void messageArrived(String topic, MqttMessage message) {
        String messageContent = new String(message.getPayload());
        logger.info(String.format("%s %s", topic, messageContent));
    }

    /**
     * Called when delivery for a message has been completed, and all acknowledgments have been received.
     * @param token the delivery token associated with the message.
     */
    @Override
    public void deliveryComplete(IMqttToken token) {
        logger.fine(String.format("Message %d was delivered.", token.getMessageId()));
    }

    /**
     * Called when the connection to the server is completed successfully.
     * @param reconnect If true, the connection was the result of automatic reconnect.
     * @param serverURI The server URI that the connection was made to.
     */
    @Override
    public void connectComplete(boolean reconnect, String serverURI) {
        logger.fine(String.format("Connection to %s complete. Reconnect=%b", serverURI, reconnect));
    }

    /**
     * Called when an AUTH packet is received by the client.
     * @param reasonCode The Reason code, can be Success (0), Continue authentication (24) or Re-authenticate (25).
     * @param properties containing the Authentication Method, Authentication Data and any required User Defined Properties.
     */
    @Override
    public void authPacketArrived(int reasonCode, MqttProperties properties) {
        logger.severe(String.format("Auth packet received, this client does not currently support them. Reason Code: %d.", reasonCode));
    }
}
