package com.example.neelanshsethi.recko.Misc;

import org.apache.commons.lang3.StringUtils;

public class Constants {
    public static final int max_company_height_in_explore = 300;
    public static final int add_lead_request_id = 7;
    public static final String edit_lead_title = "Edit Lead";
    public static final int edit_active_lead_request_id = 100;
    public static boolean isValidURL(String url) {
        return !StringUtils.isEmpty(url) && !url.equals("null");
    }
}
