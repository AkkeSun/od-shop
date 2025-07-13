package com.productagent.application.port.out;

import com.productagent.domain.model.DlqLog;
import com.productagent.domain.model.ProductHistory;
import java.util.List;

public interface LogStoragePort {

    void registerHistories(List<ProductHistory> histories);

    void registerDlqLog(DlqLog log);

    void registerDlqLogs(List<DlqLog> logs);
}
