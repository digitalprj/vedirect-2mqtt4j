package info.digitalproject.vedirect2mqtt4j.exceptions;

/**
 * Thrown if an invalid port exception occurs
 */
public class VeDirectInvalidPortException extends VeDirect2MqttException{
    public VeDirectInvalidPortException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
