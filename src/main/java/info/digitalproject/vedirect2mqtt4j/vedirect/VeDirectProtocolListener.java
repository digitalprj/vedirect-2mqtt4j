package info.digitalproject.vedirect2mqtt4j.vedirect;

/**
 * VE.Direct protocol listener interface
 */
public interface VeDirectProtocolListener {

    /**
     * Called by VeDirectProtocolHandler when a valid record is received from VE.Direct.
     * @param record holding the received VE.Direct field names and values organized as record
     */
    void onRecordReceived(VeDirectRecord record);
}
