package com.example.neelanshsethi.recko.Model;

public class Category_InCategoryAndCompanyModel {


    private String industry;
    private String category_uuid;
    private String image_url;
    private String name;
    private String max_total_commission;

    public Category_InCategoryAndCompanyModel(String industry, String category_uuid, String image_url, String name) {
        this.industry = industry;
        this.category_uuid = category_uuid;
        this.image_url = image_url;
        this.name = name;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getCategory_uuid() {
        return category_uuid;
    }

    public void setCategory_uuid(String category_uuid) {
        this.category_uuid = category_uuid;
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

    public String getMax_total_commission() {
        return max_total_commission;
    }

    public void setMax_total_commission(String max_total_commission) {
        this.max_total_commission = max_total_commission;
    }

}
