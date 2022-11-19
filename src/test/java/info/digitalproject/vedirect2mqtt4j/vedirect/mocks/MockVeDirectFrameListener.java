package info.digitalproject.vedirect2mqtt4j.vedirect.mocks;

import info.digitalproject.vedirect2mqtt4j.vedirect.VeDirectFrameListener;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class MockVeDirectFrameListener implements VeDirectFrameListener {
    Logger logger = Logger.getLogger(VeDirectFrameListener.class.getName());

    List<String> records = new ArrayList<>();

    @Override
    public void onTextRx(String name, String value) {
        logger.info("onTextRx: "+name+" "+value);
        records.add("onTextRx: "+name+" "+value);
    }

    @Override
    public void onHexRx(byte inbyte) {
        logger.info(String.format("onHexRx: %02X", inbyte));
        records.add(String.format("onHexRx: %02X", inbyte));
    }

    @Override
    public void onFrameEndEvent(boolean valid) {
        logger.info("onFrameEndEvent: "+valid);
        records.add("onFrameEndEvent: "+valid);
    }

    public String getRecord(int index) {
        return records.get(index);
    }

    public int getNumRecords() {
        return records.size();
    }

    public void clearRecords() { records.clear();}
}
