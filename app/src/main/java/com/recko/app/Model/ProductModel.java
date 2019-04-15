package com.recko.app.Model;

import android.util.Log;

import com.recko.app.Misc.Constants;

import org.json.JSONObject;

import java.io.Serializable;

public class ProductModel implements Serializable{

    private static String TAG = ProductModel.class.getSimpleName();

    private String broucher;
    private String video;
    private String to_date;
    private String good_time_to_sell;
    private String category;
    private String title;
    private String upfront_commission;
    private String from_date;
    private String location_of_sell;
    private String target_customer;
    private String type;
    private String price_on_x;
    private String company_uuid;
    private String total_commission;
    private String tips_to_sell;
    private String customer_data_needed;
    private String product_details;
    private String payment_type;
    private String product_uuid;
    private String mrp;
    private float discount;
    private String img_url;
    private String youtube_video_id;
    private String display_name;



    public ProductModel(String broucher, String video, String to_date, String good_time_to_sell, String category, String title, String upfront_commission, String from_date, String location_of_sell, String target_customer, String type, String price_on_x, String company_uuid, String total_commission, String tips_to_sell, String customer_data_needed, String product_details, String payment_type, String product_uuid, String mrp) {
        this.broucher = broucher;
        this.video = video;
        this.to_date = to_date;
        this.good_time_to_sell = good_time_to_sell;
        this.category = category;
        this.title = title;
        this.upfront_commission = upfront_commission;
        this.from_date = from_date;
        this.location_of_sell = location_of_sell;
        this.target_customer = target_customer;
        this.type = type;
        this.price_on_x = price_on_x;
        this.company_uuid = company_uuid;
        this.total_commission = total_commission;
        this.tips_to_sell = tips_to_sell;
        this.customer_data_needed = customer_data_needed;
        this.product_details = product_details;
        this.payment_type = payment_type;
        this.product_uuid = product_uuid;
        this.mrp = mrp;
    }

    public String getMrp() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
    }

    public String getBroucher() {
        return broucher;
    }

    public void setBroucher(String broucher) {
        this.broucher = broucher;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getTo_date() {
        return to_date;
    }

    public void setTo_date(String to_date) {
        this.to_date = to_date;
    }

    public String getGood_time_to_sell() {
        return good_time_to_sell;
    }

    public void setGood_time_to_sell(String good_time_to_sell) {
        this.good_time_to_sell = good_time_to_sell;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUpfront_commission() {
        return upfront_commission;
    }

    public void setUpfront_commission(String upfront_commission) {
        this.upfront_commission = upfront_commission;
    }

    public String getFrom_date() {
        return from_date;
    }

    public void setFrom_date(String from_date) {
        this.from_date = from_date;
    }

    public String getLocation_of_sell() {
        return location_of_sell;
    }

    public void setLocation_of_sell(String location_of_sell) {
        this.location_of_sell = location_of_sell;
    }

    public String getTarget_customer() {
        return target_customer;
    }

    public void setTarget_customer(String target_customer) {
        this.target_customer = target_customer;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPrice_on_x() {
        return price_on_x;
    }

    public void setPrice_on_x(String price_on_x) {
        this.price_on_x = price_on_x;
    }

    public String getCompany_uuid() {
        return company_uuid;
    }

    public void setCompany_uuid(String company_uuid) {
        this.company_uuid = company_uuid;
    }

    public String getTotal_commission() {
        return total_commission;
    }

    public void setTotal_commission(String total_commission) {
        this.total_commission = total_commission;
    }

    public String getTips_to_sell() {
        return tips_to_sell;
    }

    public void setTips_to_sell(String tips_to_sell) {
        this.tips_to_sell = tips_to_sell;
    }

    public String getCustomer_data_needed() {
        return customer_data_needed;
    }

    public void setCustomer_data_needed(String customer_data_needed) {
        this.customer_data_needed = customer_data_needed;
    }

    public String getProduct_details() {
        return product_details;
    }

    public void setProduct_details(String product_details) {
        this.product_details = product_details;
    }

    public String getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
    }

    public String getProduct_uuid() {
        return product_uuid;
    }

    public void setProduct_uuid(String product_uuid) {
        this.product_uuid = product_uuid;
    }

    public float getPrice_on_x_float() {
        float price_float = Float.parseFloat(getPrice_on_x());
        return price_float;
    }

    public String getDiscount_byRecko() {
        return Constants.fixDoubleString(
                Double.toString(Float.parseFloat(getMrp()) - getPrice_on_x_float()));

    }

    public void setDiscount(float val) {discount = val;}

    public boolean setDiscount(String val) {
        try {
            float tmp = Float.parseFloat(val);
            discount = tmp;
        } catch (Exception e){
            return false;
        }
        return true;
    }

    public float getUserPriceFloat() {
        return  getPrice_on_x_float() - discount;
    }

    public String getUserPriceString() {
        return Float.toString(getUserPriceFloat());
    }

    public String commissionAfterDiscount() {
        return Constants.fixDoubleString(
                Double.toString(Double.parseDouble(getTotal_commission()) - discount));
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }


    public String getYoutube_video_id() {
        return youtube_video_id;
    }

    public String getProductDisplayNmae() {
        if (Constants.isValidString(display_name))
            return display_name;
        return getTitle();
    }

    public void fillMoreData(JSONObject data) {
        Log.d(TAG, "Filling more data");
        Log.d(TAG, data.toString());
        try {
            if (data.has("display_name")) {
                String displayName = data.getString("display_name");
                if (Constants.isValidString(displayName)) {
                    displayName = displayName.replace("<br/>", "\n");
                    Log.d(TAG, "Display name: " + displayName);
                    display_name = displayName;
                }
            }
            img_url = !data.has("img_url")?"null":data.getString("img_url");
        } catch (Exception e) {}
    }
}
