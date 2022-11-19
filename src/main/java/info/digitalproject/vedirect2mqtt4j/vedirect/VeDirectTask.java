package info.digitalproject.vedirect2mqtt4j.vedirect;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortInvalidPortException;
import info.digitalproject.vedirect2mqtt4j.VeDirect2MqttOptions;
import info.digitalproject.vedirect2mqtt4j.exceptions.VeDirectReadException;

import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

/**
 * Handles the VE.Direct communication by reading the bytes from a serial port
 * and forward the bytes to the VE.Direct protocol handler.
 */
public class VeDirectTask implements Callable<Void> {
    /**
     * Log4j logger
     */
    private static final Logger logger = Logger.getLogger(VeDirectTask.class.getName());

    /**
     * Serial port where the VE.Direct device is connected to
     */
    private final SerialPort comPort;

    /**
     * VE.Direct protocol listener
     */
    private final VeDirectProtocolListener veDirectProtocolListener;

    /**
     * Creates a new instance of the VE.Direct communication task. Configures the serial port.
     * @param options all options needed to configure the serial port
     * @param veDirectProtocolListener the listener of the VE.Direct protocol
     * @throws SerialPortInvalidPortException if an serial port is invalid
     */
    public VeDirectTask(VeDirect2MqttOptions options, VeDirectProtocolListener veDirectProtocolListener) throws SerialPortInvalidPortException {
        comPort = SerialPort.getCommPort(options.getVeDirectPortName());
        comPort.setBaudRate(19200);
        comPort.setNumDataBits(8);
        comPort.setParity(SerialPort.NO_PARITY);
        comPort.setNumStopBits(1);
        comPort.setFlowControl(SerialPort.FLOW_CONTROL_DISABLED);
        comPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 1000, 0);

        this.veDirectProtocolListener = veDirectProtocolListener;
    }

    /**
     * Background task reading bytes from serial port and forward the bytes to the
     * VE.Direct protocol handler
     * @return nothing
     * @throws VeDirectReadException if error during reading of bytes occurs
     */
    @Override
    public Void call() throws VeDirectReadException {
        logger.info("Open VE.Direct port "+comPort.getSystemPortName());
        comPort.openPort();

        VeDirectProtocolHandler vedirectProtocolHandler = new VeDirectProtocolHandler();
        vedirectProtocolHandler.setListener(veDirectProtocolListener);

        while (!Thread.currentThread().isInterrupted()) {
            byte[] readBuffer = new byte[1024];
            int numRead = comPort.readBytes(readBuffer, readBuffer.length);

            if (numRead == -1) {
                throw new VeDirectReadException("Error reading bytes from serial port");
            }

            if (numRead > 0) {
                vedirectProtocolHandler.processBytes(Arrays.copyOfRange(readBuffer, 0, numRead));
            }
        }

        comPort.closePort();
        logger.info("Closed VE.Direct port "+comPort.getSystemPortName());

        return null;
    }

}
