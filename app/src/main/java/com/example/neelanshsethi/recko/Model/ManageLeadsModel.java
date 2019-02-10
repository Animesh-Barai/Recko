package com.example.neelanshsethi.recko.Model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class ManageLeadsModel implements Serializable {

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getLead_uuid() {
        return lead_uuid;
    }

    public void setLead_uuid(String lead_uuid) {
        this.lead_uuid = lead_uuid;
    }

    public String getContact_no() {
        return contact_no;
    }

    public void setContact_no(String contact_no) {
        this.contact_no = contact_no;
    }

    public String getContact_name() {
        return contact_name;
    }

    public void setContact_name(String contact_name) {
        this.contact_name = contact_name;
    }

    public String getDeadline_time() {
        return deadline_time;
    }

    public void setDeadline_time(String deadline_time) {
        this.deadline_time = deadline_time;
    }

    public String getCreation_time() {
        return creation_time;
    }

    public void setCreation_time(String creation_time) {
        this.creation_time = creation_time;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProduct_uuid() {
        return product_uuid;
    }

    public void setProduct_uuid(String product_uuid) {
        this.product_uuid = product_uuid;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    String comment;
    String lead_uuid;
    String contact_no;
    String contact_name;
    String deadline_time;
    String creation_time;
    String seller;
    String email;
    String product_uuid;
    String product_name;

    public ManageLeadsModel(String comment, String lead_uuid, String contact_no, String contact_name, String deadline_time, String creation_time, String seller, String email, String product_uuid, String product_name) {
        this.comment = comment;
        this.lead_uuid = lead_uuid;
        this.contact_no = contact_no;
        this.contact_name = contact_name;
        this.deadline_time = deadline_time;
        this.creation_time = creation_time;
        this.seller = seller;
        this.email = email;
        this.product_uuid = product_uuid;
        this.product_name = product_name;
    }


    public ManageLeadsModel(JSONObject data) throws JSONException {
        /*      "creation_time": "2019-02-10T09:20:35.412231",
                "seller": "3493",
                "comment": null,
                "lead_uuid": "lead-805090150a4c46cfa8fa45cdea24534c",
                "product": null,
                "deadline_time": "2019-02-08T00:00:00",
                "contact_name": "Khagesh",
                "amount_communicated": null,
                "product_uuid": null,
                "contact_no": null,
                "hidden": false,
                "email": null
        */
        this.comment = !data.has("comment")?"null":data.getString("comment");
        this.lead_uuid = !data.has("lead_uuid")?"null":data.getString("lead_uuid");
        this.contact_no = !data.has("contact_no")?"null":data.getString("contact_no");
        this.contact_name = !data.has("contact_name")?"null":data.getString("contact_name");
        this.deadline_time = !data.has("deadline_time")?"null":data.getString("deadline_time");
        this.creation_time = !data.has("creation_time")?"null":data.getString("creation_time");;
        this.seller = !data.has("seller")?"null":data.getString("seller");
        this.email = !data.has("email")?"null":data.getString("email");
        this.product_uuid = !data.has("product_uuid")?"null":data.getString("product_uuid");
        try {
            JSONObject product = data.getJSONObject("product");
            this.product_name = product.getString("title");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
