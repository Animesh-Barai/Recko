package com.example.neelanshsethi.recko.Misc;

import android.util.Log;
import android.util.Patterns;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Constants {
    public static final int max_company_height_in_explore = 300;
    public static final int add_lead_request_id = 7;
    public static final String edit_lead_title = "Edit Lead";
    public static final int edit_active_lead_request_id = 100;
    public static final int edit_missed_lead_request_id = 101;
    public static final int edit_user_info_request_id = 102;
    public static final int edit_account_info_request_id = 103;
    public static final int pick_contact_request_id = 104;

    public static boolean isValidURL(String url) {
        return !StringUtils.isEmpty(url) && !url.equals("null");
    }

    public static String fixNullString(String text) {
        if (text.equals("null")) return "";
        return text;
    }

    public static boolean validateEmail(String emailStr) {
        return (!emailStr.trim().equals("") && Patterns.EMAIL_ADDRESS.matcher(emailStr).matches());
    }
}
