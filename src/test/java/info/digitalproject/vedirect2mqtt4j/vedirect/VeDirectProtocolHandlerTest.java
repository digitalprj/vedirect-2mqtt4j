package info.digitalproject.vedirect2mqtt4j.vedirect;

import info.digitalproject.vedirect2mqtt4j.vedirect.data.DataHelper;
import info.digitalproject.vedirect2mqtt4j.vedirect.mocks.MockVeDirectProtocolListener;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * Test naming convention: MethodName_StateUnderTest_ExpectedBehavior
 */
public class VeDirectProtocolHandlerTest {
    @Test
    public void processBytes_BmvData_ValidRecord() throws IOException {

        MockVeDirectProtocolListener mockProtocolListener = new MockVeDirectProtocolListener();

        VeDirectProtocolHandler veDirectProtocolHandler = new VeDirectProtocolHandler();
        veDirectProtocolHandler.setListener(mockProtocolListener);

        veDirectProtocolHandler.processBytes(DataHelper.read("testdata_bmv_01.bin"));

        Assertions.assertEquals("0x203", mockProtocolListener.getRecord(0).get("PID"));
        Assertions.assertEquals("26201", mockProtocolListener.getRecord(0).get("V"));
        Assertions.assertEquals("0", mockProtocolListener.getRecord(0).get("I"));
        Assertions.assertEquals("0", mockProtocolListener.getRecord(0).get("P"));
        Assertions.assertEquals("0", mockProtocolListener.getRecord(0).get("CE"));
        Assertions.assertEquals("1000", mockProtocolListener.getRecord(0).get("SOC"));
        Assertions.assertEquals("-1", mockProtocolListener.getRecord(0).get("TTG"));
        Assertions.assertEquals("OFF", mockProtocolListener.getRecord(0).get("Alarm"));
        Assertions.assertEquals("OFF", mockProtocolListener.getRecord(0).get("Relay"));
        Assertions.assertEquals("0", mockProtocolListener.getRecord(0).get("AR"));
        Assertions.assertEquals("700", mockProtocolListener.getRecord(0).get("BMV"));
        Assertions.assertEquals("0307", mockProtocolListener.getRecord(0).get("FW"));

    }

    @Test
    public void processBytes_MppData_ValidRecord() throws IOException {

        MockVeDirectProtocolListener mockProtocolListener = new MockVeDirectProtocolListener();

        VeDirectProtocolHandler veDirectProtocolHandler = new VeDirectProtocolHandler();
        veDirectProtocolHandler.setListener(mockProtocolListener);

        veDirectProtocolHandler.processBytes(DataHelper.read("testdata_mppt_01.bin"));

        Assertions.assertEquals("0xA060", mockProtocolListener.getRecord(0).get("PID"));
        Assertions.assertEquals("159", mockProtocolListener.getRecord(0).get("FW"));
        Assertions.assertEquals("HQ2119QGHQ6", mockProtocolListener.getRecord(0).get("SER#"));
        Assertions.assertEquals("26340", mockProtocolListener.getRecord(0).get("V"));
        Assertions.assertEquals("1050", mockProtocolListener.getRecord(0).get("I"));
        Assertions.assertEquals("34920", mockProtocolListener.getRecord(0).get("VPV"));
        Assertions.assertEquals("29", mockProtocolListener.getRecord(0).get("PPV"));
        Assertions.assertEquals("3", mockProtocolListener.getRecord(0).get("CS"));
        Assertions.assertEquals("2", mockProtocolListener.getRecord(0).get("MPPT"));
        Assertions.assertEquals("0x00000000", mockProtocolListener.getRecord(0).get("OR"));
        Assertions.assertEquals("0", mockProtocolListener.getRecord(0).get("ERR"));
        Assertions.assertEquals("ON", mockProtocolListener.getRecord(0).get("LOAD"));
        Assertions.assertEquals("0", mockProtocolListener.getRecord(0).get("IL"));
        Assertions.assertEquals("6262", mockProtocolListener.getRecord(0).get("H19"));
        Assertions.assertEquals("81", mockProtocolListener.getRecord(0).get("H20"));
        Assertions.assertEquals("247", mockProtocolListener.getRecord(0).get("H21"));
        Assertions.assertEquals("20", mockProtocolListener.getRecord(0).get("H22"));
        Assertions.assertEquals("217", mockProtocolListener.getRecord(0).get("H23"));
        Assertions.assertEquals("195", mockProtocolListener.getRecord(0).get("HSDS"));
    }
}