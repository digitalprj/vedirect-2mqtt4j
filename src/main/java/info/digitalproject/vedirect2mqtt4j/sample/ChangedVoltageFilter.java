package info.digitalproject.vedirect2mqtt4j.sample;

import info.digitalproject.vedirect2mqtt4j.PublishFilter;
import info.digitalproject.vedirect2mqtt4j.vedirect.VeDirectRecord;

import java.util.logging.Logger;

/**
 * MQTT record publish filter ensure that MQTT messages are only published if:
 * 1. at least one minute has elapsed since the last published messages.
 * 2. the voltage has changed for at leased MIN_VOLTAGE_CHANGES
 */
public class ChangedVoltageFilter  implements PublishFilter {
    /**
     * the logger
     */
    private Logger logger = java.util.logging.Logger.getLogger(ChangedVoltageFilter.class.getName());

    /**
     * MQTT message publish interval
     */
    private static final long PUBLISH_INTERVAL = 60000L;

    /**
     * minimum count of voltage changes
     */
    public static final int MIN_VOLTAGE_CHANGES = 5;

    /**
     * current count of voltage changes
     */
    int numVoltageChanges = MIN_VOLTAGE_CHANGES -1;

    /**
     * last voltage
     */
    int lastVoltage = 0;

    /**
     * Timestamp of next MQTT message publishing
     */
    long nextPublishTimeMillis = 0;

    /**
     * Execute the MQTT record filter
     * @param record incoming MQTT record
     * @return null, if this MQTT record shall not be published
     */
    @Override
    public VeDirectRecord execute(VeDirectRecord record) {

        // time to publish?
        if (nextPublishTimeMillis <= System.currentTimeMillis()) {

            // get current voltage
            String voltageStr = (String) record.get("V");
            if (voltageStr != null && voltageStr.length() > 0) {

                // round it
                int voltage = Integer.parseInt(voltageStr);
                voltage = (voltage + 50) / 100;

                // count the voltage changes
                if (voltage != lastVoltage) {
                    numVoltageChanges++;
                } else {
                    numVoltageChanges = 0;
                }

                // enough voltage changes?
                if (numVoltageChanges == MIN_VOLTAGE_CHANGES) {
                    // ...yes, publish!
                    logger.info("Voltage changed from " + lastVoltage + " to " + voltage + " publish MQTT Message!");

                    // modify the pid of the record
                    if(record.get("PID").equals("0xA060")) {
                        record.put("PID", "SmartSolar MPPT 100|20 48V");
                    }

                    // store values for next run
                    lastVoltage = voltage;
                    numVoltageChanges = 0;
                    nextPublishTimeMillis = System.currentTimeMillis() + PUBLISH_INTERVAL;

                    return record;
                }
            }
        }

        return null;
    }
}
