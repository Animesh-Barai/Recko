package com.example.neelanshsethi.recko.Misc;

import android.content.SharedPreferences;
import android.util.Log;
import android.util.Patterns;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.appcompat.app.AppCompatActivity;

public class Constants {
    public static final int max_company_height_in_explore = 300;
    public static final int add_lead_request_id = 7;
    public static final String edit_lead_title = "Edit Lead";
    public static final int edit_active_lead_request_id = 100;
    public static final int edit_missed_lead_request_id = 101;
    public static final int edit_user_info_request_id = 102;
    public static final int edit_account_info_request_id = 103;
    public static final int pick_contact_request_id = 104;

    public static SharedPreferences sharedPreferences;

    public static String seller_name;

    public static String seller_mobile_no;

    public static boolean isValidURL(String url) {
        return url!=null && !StringUtils.isEmpty(url) && !url.equals("null");
    }

    public static String fixNullString(String text) {
        if (text.equals("null")) return "";
        return text;
    }

    public static boolean validateEmail(String emailStr) {
        return (!emailStr.trim().equals("") && Patterns.EMAIL_ADDRESS.matcher(emailStr).matches());
    }

    public static boolean validIndianNumber(String number) {
        if (number.trim().equals("")) return false;
        String pattern = "^(?:(?:\\+|0{0,2})91(\\s*[\\ -]\\s*)?|[0]?)?[789]\\d{9}|(\\d[ -]?){10}\\d$";

        // Create a Pattern object
        Pattern r = Pattern.compile(pattern);

        // Now create matcher object.
        Matcher m = r.matcher(number);
        return m.matches();
    }

    public static String percentageValueDiscount(String val, String per) {
        Double tVal = Double.parseDouble(val);
        Double tPer = Double.parseDouble(per);
        return fixDoubleString(Double.toString(tVal*((tPer)/100.0)));
    }

    public static String fixDoubleString(String str) {
        Double val = Double.parseDouble(str);
        int val_int = (int)Math.ceil(val);
        return Integer.toString(val_int);
    }

    public static void MaybeCreateSharedPreference(AppCompatActivity activity) {}

    public static void storeUserNameInPreference() {}

    public static String getSeller_mobile_no() {
        return seller_mobile_no;
    }

    public static void setSeller_mobile_no(String seller_mobile_no) {
        Constants.seller_mobile_no = seller_mobile_no;
    }

    public static String getSeller_name() {
        return seller_name;
    }

    public static void setSeller_name(String seller_name) {
        Constants.seller_name = seller_name;
    }

    public static void setSellerNameNo(String seller_name, String seller_mobile_no) {
        Constants.setSeller_name(seller_name);
        Constants.setSeller_mobile_no(seller_mobile_no);
    }
}
