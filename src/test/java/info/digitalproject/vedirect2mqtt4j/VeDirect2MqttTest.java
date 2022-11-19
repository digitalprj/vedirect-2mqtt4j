package info.digitalproject.vedirect2mqtt4j;

import info.digitalproject.vedirect2mqtt4j.exceptions.MqttClientException;
import info.digitalproject.vedirect2mqtt4j.exceptions.VeDirectInvalidPortException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.logging.Logger;

/**
 * Test naming convention: MethodName_StateUnderTest_ExpectedBehavior
 */
class VeDirect2MqttTest {

    Logger logger = Logger.getLogger(VeDirect2MqttTest.class.getName());

    @Test
    public void runAsync_InvalidPortName_VeDirectInvalidPortException() {

        Exception e = Assertions.assertThrows(VeDirectInvalidPortException.class, () -> {
            VeDirect2MqttOptions options = new VeDirect2MqttOptions.Builder("invalid", "tcp://invalid.de")
                    .mqttPermanentConnection(false)
                    .build();
            VeDirect2Mqtt veDirect2MQTT = new VeDirect2Mqtt(options);
            veDirect2MQTT.runAsync();
        });

        logger.info(e.getMessage());
    }
}