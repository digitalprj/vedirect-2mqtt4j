package info.digitalproject.vedirect2mqtt4j.sample;

import info.digitalproject.vedirect2mqtt4j.PublishFilter;
import info.digitalproject.vedirect2mqtt4j.vedirect.VeDirectRecord;

import java.util.logging.Logger;

/**
 * MQTT record publish filter ensure that MQTT messages are only published if
 * the simple moving average of battery voltage is different to the last published
 */
public class AverageVoltageFilter implements PublishFilter {
    /**
     * the logger
     */
    private final Logger logger = java.util.logging.Logger.getLogger(AverageVoltageFilter.class.getName());

    /**
     * Simple moving average of battery voltage
     */
    private final SimpleMovingAverage avgVoltage;

    /**
     * previous published battery voltage
     */
    private int preVoltage = 0;

    /**
     * Create new AverageVoltageFilter
     */
    public AverageVoltageFilter() {
        avgVoltage = new SimpleMovingAverage(60);
    }

    /**
     * Execute the publish filter.
     * Get battery voltage from incoming VE.Direct record,
     * compute moving average and publish if average is different
     * to previous voltage
     * @param record record to be published
     * @return modified record that shall be published. Return null if this record shall not be published.
     */
    @Override
    public VeDirectRecord execute(VeDirectRecord record) {

        // get current voltage
        String voltageStr = (String) record.get("V");
        if (voltageStr != null && voltageStr.length() > 0) {

            // parse to integer
            int voltage = Integer.parseInt(voltageStr);
            logger.fine("Voltage: " + voltage + " size: "+avgVoltage.dataset.size());

            // add to moving average
            avgVoltage.addData(voltage);

            // get the mean value and divide by 10 to get rid of high frequent changes
            int mean = avgVoltage.getMean() / 10;

            // mean differs from previous published voltage?
            if (mean != preVoltage) {
                // ...yes, publish the record to MQTT
                logger.info("Voltage changed "+preVoltage+" -> "+mean+" ...publish record!");

                // set mean voltage to current record
                record.put("V", mean * 10);

                // store current mean for next run
                preVoltage = mean;

                // publish record
                return record;
            }
        }

        // do not publish record
        return null;
    }
}
