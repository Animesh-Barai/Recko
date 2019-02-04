package com.example.neelanshsethi.recko.Model;

import android.graphics.drawable.Drawable;

public class IndustryChipModel {

    private Drawable industry_logo;
    private String industry_name;
    private String industry_uuid;
    private Boolean isSelected;

    public IndustryChipModel(Drawable industry_logo, String industry_name, String industry_uuid) {
        this.industry_logo = industry_logo;
        this.industry_name = industry_name;
        this.industry_uuid = industry_uuid;
        this.isSelected=false;

    }
    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }

    public Drawable getIndustry_logo() {
        return industry_logo;
    }

    public void setIndustry_logo(Drawable industry_logo) {
        this.industry_logo = industry_logo;
    }

    public String getIndustry_name() {
        return industry_name;
    }

    public void setIndustry_name(String industry_name) {
        this.industry_name = industry_name;
    }

    public String getIndustry_uuid() {
        return industry_uuid;
    }

    public void setIndustry_uuid(String industry_uuid) {
        this.industry_uuid = industry_uuid;
    }
}
