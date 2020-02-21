package com.recko.app.Model;

public class IndustryCardModel {

    private String image_url;
    private String name;
    private String industry_uuid;
    private String large_image_url;
    private boolean should_use_large_image;

    public IndustryCardModel(String image_url, String name, String industry_uuid) {
        this.image_url = image_url;
        this.name = name;
        this.industry_uuid = industry_uuid;
        should_use_large_image = false;
    }

    public IndustryCardModel(String image_url, String name, String industry_uuid, String large_image_url) {
        this.image_url = image_url;
        this.name = name;
        this.industry_uuid = industry_uuid;
        this.large_image_url = large_image_url;
        should_use_large_image = false;
    }

    public String getImage_url() {
        if (should_use_large_image) return large_image_url;
        return image_url;
    }

    public void setShould_use_large_image(boolean val) {
        should_use_large_image = val;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIndustry_uuid() {
        return industry_uuid;
    }

    public void setIndustry_uuid(String industry_uuid) {
        this.industry_uuid = industry_uuid;
    }


}
