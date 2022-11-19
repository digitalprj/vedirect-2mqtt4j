package info.digitalproject.vedirect2mqtt4j.vedirect;

/**
 * VE.Direct protocol handler forwards the received bytes from the com port to the frame handler.
 * Implements frame handler callbacks to stores received field values as record.
 * Notifies protocol listeners when a valid record is received.
 */
public class VeDirectProtocolHandler implements VeDirectFrameListener {

    /**
     * VE.Direct frame handler
     */
    private VeDirectFramehandler veDirectFramehandler;

    /**
     * VE.Direct record holding field names and values
     */
    private VeDirectRecord record;

    /**
     * VE.Direct protocol listener
     */
    private VeDirectProtocolListener listener;

    /**
     * Creates a VE.Direct protocol handler
     */
    public VeDirectProtocolHandler() {
        veDirectFramehandler = new VeDirectFramehandler();
        veDirectFramehandler.setListener(this);

        record = new VeDirectRecord();
    }

    /**
     * Called by frame handler when text field is received.
     * Put the field to the record
     * @param name of text field
     * @param value of text field
     */
    @Override
    public void onTextRx(String name, String value) {
        record.put(name, value);
    }

    /**
     * Called by frame handler when hex field is received.
     * @param inbyte hex data
     */
    @Override
    public void onHexRx(byte inbyte) {
        // do nothing
    }

    /**
     * Called by frame handler when end of frame is detected.
     * @param valid true, if frame is valid (CRC is correct)
     */
    @Override
    public void onFrameEndEvent(boolean valid) {
        if (valid) {
            if (listener != null) {
                listener.onRecordReceived(record);
            }
        }
        record = new VeDirectRecord();
    }

    /**
     * Forward the incoming bytes to the frame handler
     * @param buffer incoming bytes
     */
    public void processBytes(byte[] buffer) {
        for (byte b : buffer) {
            veDirectFramehandler.rxData(b);
        }
    }

    /**
     * Set the protocol listener
     * @param listener of the VE.Direct protocol
     */
    public void setListener(VeDirectProtocolListener listener) {
        this.listener = listener;
    }

}
