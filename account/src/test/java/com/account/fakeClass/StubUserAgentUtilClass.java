package com.account.fakeClass;

import com.account.infrastructure.util.UserAgentUtil;

public class StubUserAgentUtilClass implements UserAgentUtil {

    @Override
    public String getUserAgent() {
        return "chrome";
    }
}
