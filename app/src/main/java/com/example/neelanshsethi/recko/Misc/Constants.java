package com.example.neelanshsethi.recko.Misc;

import org.apache.commons.lang3.StringUtils;

public class Constants {
    public static final int max_company_height_in_explore = 300;

    public static boolean isValidURL(String url) {
        return !StringUtils.isEmpty(url) && !url.equals("null");
    }
}
