package com.accountagent.application.port.out;

public interface TokenStoragePort {

    void deleteByRegDateTimeBetween(String start, String end);
}
