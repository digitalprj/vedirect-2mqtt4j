package info.digitalproject.vedirect2mqtt4j.vedirect;

/**
 * VE.Direct protocol frame listener interface
 */
public interface VeDirectFrameListener {
    /**
     * Called by frame handler when text field is received.
     * Put the field to the record
     * @param name of text field
     * @param value of text field
     */
    void onTextRx(String name, String value);

    /**
     * Called by frame handler when hex field is received.
     * @param inbyte hex data
     */
    void onHexRx(byte inbyte);

    /**
     * Called by frame handler when end of frame is detected.
     * @param valid true, if frame is valid (CRC is correct)
     */
    void onFrameEndEvent(boolean valid);
}
