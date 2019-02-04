package com.example.neelanshsethi.recko.Model;

public class IndustryCardModel {

    private String image_url;
    private String name;
    private String industry_uuid;

    public IndustryCardModel(String image_url, String name, String industry_uuid) {
        this.image_url = image_url;
        this.name = name;
        this.industry_uuid = industry_uuid;
    }

    public String getImage_url() {
        return image_url;
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
