package info.digitalproject.vedirect2mqtt4j.vedirect.mocks;

import info.digitalproject.vedirect2mqtt4j.vedirect.VeDirectProtocolListener;
import info.digitalproject.vedirect2mqtt4j.vedirect.VeDirectRecord;
import java.util.ArrayList;
import java.util.List;

public class MockVeDirectProtocolListener implements VeDirectProtocolListener {

    private List<VeDirectRecord> recordList;

    public MockVeDirectProtocolListener() {
        recordList = new ArrayList<>();
    }

    @Override
    public void onRecordReceived(VeDirectRecord record) {
        recordList.add(record);
    }

    public VeDirectRecord getRecord(int index) {
        return recordList.get(index);
    }
}
