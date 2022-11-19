package info.digitalproject.vedirect2mqtt4j.exceptions;

/**
 * Thrown if a exception during reading of VE.Direct port occurs
 */
public class VeDirectReadException extends VeDirect2MqttException {
    public VeDirectReadException(String message)
    {
        super(message);
    }
}
