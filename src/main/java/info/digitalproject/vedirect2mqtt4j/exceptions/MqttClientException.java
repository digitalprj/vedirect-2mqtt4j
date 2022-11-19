package info.digitalproject.vedirect2mqtt4j.exceptions;

/**
 * Thrown if a MQTT client exception occurs
 */
public class MqttClientException extends VeDirect2MqttException {
    public MqttClientException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
