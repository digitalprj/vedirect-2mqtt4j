package info.digitalproject.vedirect2mqtt4j.vedirect;


import java.util.Locale;
import java.util.logging.Logger;

/**
 * Implementation of the VE.Direct frame handler
 * based on the VE.Direct Protocol FAQ reference implementation
 */
public class VeDirectFramehandler {

    /**
     * Log4j logger
     */
    Logger logger = Logger.getLogger(VeDirectFramehandler.class.getName());

    /**
     * Checksum TAG name
     */
    private static final String CHECKSUM_TAG_NAME = "CHECKSUM";

    /**
     * States of the frame handler state machine
     */
    enum FrameHandlerState {
        IDLE,
        RECORD_BEGIN,
        RECORD_NAME,
        RECORD_VALUE,
        CHECKSUM,
        RECORD_HEX
    }

    /**
     * Current state of the state machine
     */
    private FrameHandlerState frameHandlerState;

    /**
     * Checksum of the frame
     */
    private byte checksum;

    /**
     * Field name buffer
     */
    private StringBuilder fieldName;

    /**
     * Field value buffer
     */
    private StringBuilder fieldValue;

    /**
     * Frame handler listener
     */
    private VeDirectFrameListener listener;

    /**
     * Raw data buffer
     */
    private StringBuilder rawData;

    /**
     * Creates a VE.Direct frame handler
     */
    public VeDirectFramehandler() {
        frameHandlerState = FrameHandlerState.IDLE;
        checksum = 0;
        fieldName = new StringBuilder();
        fieldValue = new StringBuilder();
        rawData = new StringBuilder();
    }

    /**
     * Set the VE.Direct frame listener
     * @param listener frame listener
     */
    public void setListener(VeDirectFrameListener listener) {
        this.listener = listener;
    }

    /**
     * Process incoming bytes on the state machine.
     * Evaluate VE.Direct protocol frames, notifies frame handler listeners
     * about reception of text or hex fields and record validation
     * @param inbyte byte to process
     */
    public void rxData(byte inbyte) {

        logRawData(inbyte);

        if ( (inbyte == ':') && (frameHandlerState != FrameHandlerState.CHECKSUM) ) {
            frameHandlerState = FrameHandlerState.RECORD_HEX;
        }
        if (frameHandlerState != FrameHandlerState.RECORD_HEX) {
            checksum += inbyte;
        }
        //inbyte = (byte) Character.toUpperCase(inbyte);

        switch(frameHandlerState) {
            case IDLE:
                /* wait for \n of the start of an record */
                switch (inbyte) {
                    case '\n':
                        frameHandlerState = FrameHandlerState.RECORD_BEGIN;
                        break;
                    case '\r': /* Skip */
                    default:
                        break;
                }
                break;

            case RECORD_BEGIN:
                fieldName = new StringBuilder();
                fieldName.append((char)inbyte);
                frameHandlerState = FrameHandlerState.RECORD_NAME;
                break;

            case RECORD_NAME:
                // The record name is being received, terminated by a \t
                if (inbyte == '\t') {// the Checksum record indicates a EOR
                    if (fieldName.toString().toUpperCase(Locale.ROOT).equals(CHECKSUM_TAG_NAME)) {
                        frameHandlerState = FrameHandlerState.CHECKSUM;
                        break;
                    }
                    fieldValue = new StringBuilder(); /* Reset value pointer */
                    frameHandlerState = FrameHandlerState.RECORD_VALUE;
                } else {// add byte to name
                    fieldName.append((char) inbyte);
                }
                break;

            case RECORD_VALUE:
                // The record value is being received. The \r indicates a new record.
                switch(inbyte) {
                    case '\n':
                        // forward record, only if it could be stored completely
                        if(listener != null) {
                            listener.onTextRx(fieldName.toString(), fieldValue.toString());
                        }
                        frameHandlerState = FrameHandlerState.RECORD_BEGIN;
                        break;
                    case '\r': /* Skip */
                        break;
                    default:
                        // add byte to value
                        fieldValue.append((char)inbyte);
                        break;
                }
                break;

            case CHECKSUM:
            {
                boolean valid = checksum == 0;
                if (!valid)
                    logger.severe("[CHECKSUM] Invalid frame");
                checksum = 0;
                frameHandlerState = FrameHandlerState.IDLE;
                this.listener.onFrameEndEvent(valid);
                break;
            }

            case RECORD_HEX:
                this.listener.onHexRx(inbyte);
                if (inbyte == '\n') {
                    checksum = 0;
                    frameHandlerState = FrameHandlerState.IDLE;
                }
                break;
        }
    }

    /**
     * Helper method to log bytes as hex
     * @param inbyte byte to format as hex
     */
    private void logRawData(byte inbyte) {
        rawData.append(String.format("0x%02x, ", inbyte));
        if (rawData.length() == 16*6) {
            logger.finest(rawData.toString());
            rawData = new StringBuilder();
        }
    }
}
