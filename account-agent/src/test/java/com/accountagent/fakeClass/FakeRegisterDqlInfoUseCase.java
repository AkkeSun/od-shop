package com.accountagent.fakeClass;

import com.accountagent.application.port.in.RegisterDqlInfoUseCase;
import java.util.ArrayList;
import java.util.List;

public class FakeRegisterDqlInfoUseCase implements RegisterDqlInfoUseCase {

    public static class DlqInfo {
        public String topic;
        public String payload;

        public DlqInfo(String topic, String payload) {
            this.topic = topic;
            this.payload = payload;
        }
    }

    public List<DlqInfo> receivedDlqInfos = new ArrayList<>();

    @Override
    public void registerDqlInfo(String topic, String payload) {
        receivedDlqInfos.add(new DlqInfo(topic, payload));
    }

    public void clear() {
        receivedDlqInfos.clear();
    }
}
