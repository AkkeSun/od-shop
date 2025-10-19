package com.account.fakeClass;

import com.common.infrastructure.util.UserAgentUtil;

@Deprecated
public class StubUserAgentUtilClass implements UserAgentUtil {

    @Override
    public String getUserAgent() {
        return "";
    }
}
