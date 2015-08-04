package com.sape.pages.data;

import java.util.Map;

import com.sape.common.Constants;
import com.sape.common.Utilities;

public class Data {
    private static final Map<String, Object> MAP = Utilities.loadJsonToMap(Constants.BASE_DIR + "data.json");

    public static final String PAGE_TITLE = (String) MAP.get("pageTitle");
}
