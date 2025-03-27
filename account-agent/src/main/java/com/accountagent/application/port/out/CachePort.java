package com.accountagent.application.port.out;

import com.accountagent.domain.model.AccountHistory;
import java.util.Map;

public interface CachePort {

    Map<String, AccountHistory> findAllAccountHistory();

    void delete(String key);
}
