package com.account.fakeClass;

import com.account.infrastructure.util.UserAgentUtil;

public class FakeUserAgentUtilClass implements UserAgentUtil {

    @Override
    public String getUserAgent() {
        return "chrome";
    }
}
