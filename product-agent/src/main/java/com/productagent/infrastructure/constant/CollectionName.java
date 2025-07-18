package com.productagent.infrastructure.constant;

import static com.productagent.infrastructure.util.DateUtil.getCurrentDate;

public class CollectionName {

    public static String HISTORY() {
        return "history_" + getCurrentDate();
    }

    public static String CLICK() {
        return "click_" + getCurrentDate();
    }

    public static String SEARCH() {
        return "search_" + getCurrentDate();
    }

    public static String DLQ() {
        return "dql_" + getCurrentDate();
    }
}
