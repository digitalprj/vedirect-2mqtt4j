package info.digitalproject.vedirect2mqtt4j.vedirect;

import info.digitalproject.vedirect2mqtt4j.vedirect.data.DataHelper;
import info.digitalproject.vedirect2mqtt4j.vedirect.mocks.MockVeDirectFrameListener;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * Test naming convention: MethodName_StateUnderTest_ExpectedBehavior
 */
public class VeDirectFramehandlerTest {

    @Test
    public void rxData_BmvData_ValidRecord() throws IOException {
        VeDirectFramehandler veDirectFramehandler = new VeDirectFramehandler();
        MockVeDirectFrameListener mockListener = new MockVeDirectFrameListener();
        veDirectFramehandler.setListener(mockListener);

        for(byte inbyte : DataHelper.read("testdata_bmv_01.bin")) {
            veDirectFramehandler.rxData(inbyte);
        }

        Assertions.assertEquals(13, mockListener.getNumRecords());
        Assertions.assertTrue(mockListener.getRecord(0).contains("PID 0x203"));
        Assertions.assertTrue(mockListener.getRecord(1).contains("V 26201"));
        Assertions.assertTrue(mockListener.getRecord(2).contains("I 0"));
        Assertions.assertTrue(mockListener.getRecord(3).contains("P 0"));
        Assertions.assertTrue(mockListener.getRecord(4).contains("CE 0"));
        Assertions.assertTrue(mockListener.getRecord(5).contains("SOC 1000"));
        Assertions.assertTrue(mockListener.getRecord(6).contains("TTG -1"));
        Assertions.assertTrue(mockListener.getRecord(7).contains("Alarm OFF"));
        Assertions.assertTrue(mockListener.getRecord(8).contains("Relay OFF"));
        Assertions.assertTrue(mockListener.getRecord(9).contains("AR 0"));
        Assertions.assertTrue(mockListener.getRecord(10).contains("BMV 700"));
        Assertions.assertTrue(mockListener.getRecord(11).contains("FW 0307"));
        Assertions.assertTrue(mockListener.getRecord(12).contains("true"));

    }

    @Test
    public void rxData_MppData_ValidRecord() throws IOException {
        VeDirectFramehandler veDirectFramehandler = new VeDirectFramehandler();
        MockVeDirectFrameListener mockListener = new MockVeDirectFrameListener();
        veDirectFramehandler.setListener(mockListener);

        for(byte inbyte : DataHelper.read("testdata_mppt_01.bin")) {
            veDirectFramehandler.rxData(inbyte);
        }

        Assertions.assertEquals(816, mockListener.getNumRecords());
        Assertions.assertTrue(mockListener.getRecord(0).contains("PID 0xA060"));
        Assertions.assertTrue(mockListener.getRecord(1).contains("FW 159"));
        Assertions.assertTrue(mockListener.getRecord(2).contains("SER# HQ2119QGHQ6"));
        Assertions.assertTrue(mockListener.getRecord(3).contains("V 26340"));
        Assertions.assertTrue(mockListener.getRecord(4).contains("I 1050"));
        Assertions.assertTrue(mockListener.getRecord(5).contains("VPV 34920"));
        Assertions.assertTrue(mockListener.getRecord(6).contains("PPV 29"));
        Assertions.assertTrue(mockListener.getRecord(7).contains("CS 3"));
        Assertions.assertTrue(mockListener.getRecord(8).contains("MPPT 2"));
        Assertions.assertTrue(mockListener.getRecord(9).contains("OR 0x00000000"));
        Assertions.assertTrue(mockListener.getRecord(10).contains("ERR 0"));
        Assertions.assertTrue(mockListener.getRecord(11).contains("LOAD ON"));
        Assertions.assertTrue(mockListener.getRecord(12).contains("IL 0"));
        Assertions.assertTrue(mockListener.getRecord(13).contains("H19 6262"));
        Assertions.assertTrue(mockListener.getRecord(14).contains("H20 81"));
        Assertions.assertTrue(mockListener.getRecord(15).contains("H21 247"));
        Assertions.assertTrue(mockListener.getRecord(16).contains("H22 20"));
        Assertions.assertTrue(mockListener.getRecord(17).contains("H23 217"));
        Assertions.assertTrue(mockListener.getRecord(18).contains("HSDS 195"));
        Assertions.assertTrue(mockListener.getRecord(19).contains("true"));
    }
}