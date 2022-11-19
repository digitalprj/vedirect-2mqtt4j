package info.digitalproject.vedirect2mqtt4j;

import info.digitalproject.vedirect2mqtt4j.vedirect.VeDirectRecord;

/**
 * Use to filter VE.Direct records before publishing to MQTT
 */
public interface PublishFilter {

    /**
     * Execute the publish filter
     * @param record record to be published
     * @return modified record that shall be published. Return null if this record shall not be published.
     */
    VeDirectRecord execute(VeDirectRecord record);
}
