package com.accountagent.application.port.out;

import com.accountagent.domain.model.AccountHistory;
import java.util.Map;

public interface RedisPort {

    Map<String, AccountHistory> findAllAccountHistory();

    void delete(String key);
}
