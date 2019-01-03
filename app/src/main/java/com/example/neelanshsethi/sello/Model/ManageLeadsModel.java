package com.example.neelanshsethi.sello.Model;

public class ManageLeadsModel {

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





}
