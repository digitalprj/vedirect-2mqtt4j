package info.digitalproject.vedirect2mqtt4j.exceptions;

/**
 * VeDirect2Mqtt base exception.
 */
public class VeDirect2MqttException extends Exception {
    public VeDirect2MqttException(String message, Throwable cause) {
        super(message, cause);
    }

    public VeDirect2MqttException(String message) {
        super(message);
    }
}
