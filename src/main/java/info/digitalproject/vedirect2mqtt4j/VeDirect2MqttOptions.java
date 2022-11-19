package info.digitalproject.vedirect2mqtt4j;

/**
 * Holding all the options needed to configure the VE.Direct to MQTT transfer.
 */
public class VeDirect2MqttOptions {
    private String veDirectPortName;
    private String mqttTopic;
    private int mqttQos;
    private String mqttBrokerUri;
    private String mqttClientId;
    private boolean mqttRetained;
    private boolean mqttPermanentConnection;

    /**
     * Options builder implementing the Builder pattern
     */
    public static class Builder {
        // required
        private final String veDirectPortName;
        private final String mqttBrokerUri;
        // optional
        private String mqttTopic = "victron/device";
        private int mqttQos = 2;
        private String mqttClientId = "VeDirect2MQTT";
        private boolean mqttRetained = true;
        private boolean mqttPermanentConnection = true;

        /**
         * Create a new option builder
         * @param veDirectPortName name of serial port where a VE.Direct device is connected to
         * @param mqttBrokerUri URI of the MQTT brocker
         */
        public Builder(String veDirectPortName, String mqttBrokerUri) {
            this.veDirectPortName = veDirectPortName;
            this.mqttBrokerUri = mqttBrokerUri;
        }

        /**
         * MQTT topic name for publishing messages
         * @param value MQTT topic
         * @return builder
         */
        public Builder mqttTopic(String value){
            this.mqttTopic = value;
            return this;
        }

        /**
         * MQTT qualities of service
         * @param value qualities of service
         * @return builder
         */
        public Builder mqttQos(int value){
            this.mqttQos = value;
            return this;
        }

        /**
         * MQTT client id
         * @param value client id
         * @return builder
         */
        public Builder mqttClientId(String value){
            this.mqttClientId = value;
            return this;
        }

        /**
         * If the MQTT message is retained
         * @param value retained flag
         * @return builder
         */
        public Builder mqttRetained(boolean value) {
            this.mqttRetained = value;
            return this;
        }

        /**
         * true = open connection when starting the service. Close connection when service is stopped.
         * false = open connection before publishing a MQTT message. Close connection when MQTT message has been published.
         * @param value permanent connection flag
         * @return builder
         */
        public Builder mqttPermanentConnection(boolean value) {
            this.mqttPermanentConnection = value;
            return this;
        }

        /**
         * build the vedirect2mqtt4j options
         * @return options
         */
        public VeDirect2MqttOptions build() {
            return new VeDirect2MqttOptions(this);
        }
    }

    private VeDirect2MqttOptions(Builder builder) {
        veDirectPortName = builder.veDirectPortName;
        mqttTopic = builder.mqttTopic;
        mqttQos = builder.mqttQos;
        mqttBrokerUri = builder.mqttBrokerUri;
        mqttClientId = builder.mqttClientId;
        mqttRetained = builder.mqttRetained;
        mqttPermanentConnection = builder.mqttPermanentConnection;
    }

    public String getVeDirectPortName() {
        return veDirectPortName;
    }

    public String getMqttTopic() {
        return mqttTopic;
    }

    public int getMqttQos() {
        return mqttQos;
    }

    public String getMqttBrokerUri() {
        return mqttBrokerUri;
    }

    public String getMqttClientId() {
        return mqttClientId;
    }

    public boolean isMqttRetained() {
        return mqttRetained;
    }

    public boolean isMqttPermanentConnection() {
        return mqttPermanentConnection;
    }
}
