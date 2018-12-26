package com.example.neelanshsethi.sello.Model;

import java.util.ArrayList;
import java.util.List;

public class Company_InCategoryAndCompanyModel {

    private String max_commisiion;
    private String company_uuid;
    private String image_url;
    private String company_name;
    private String no_of_products;
    private List<ProductModel> list;

    public Company_InCategoryAndCompanyModel(String max_commisiion, String company_uuid, String image_url, String company_name, String no_of_products, List<ProductModel> list) {
        this.max_commisiion = max_commisiion;
        this.company_uuid = company_uuid;
        this.image_url = image_url;
        this.company_name = company_name;
        this.no_of_products = no_of_products;
        this.list = list;
    }

    public String getMax_commisiion() {
        return max_commisiion;
    }

    public void setMax_commisiion(String max_commisiion) {
        this.max_commisiion = max_commisiion;
    }

    public String getCompany_uuid() {
        return company_uuid;
    }

    public void setCompany_uuid(String company_uuid) {
        this.company_uuid = company_uuid;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getNo_of_products() {
        return no_of_products;
    }

    public void setNo_of_products(String no_of_products) {
        this.no_of_products = no_of_products;
    }

    public List<ProductModel> getList() {
        return list;
    }

    public void setList(List<ProductModel> list) {
        this.list = list;
    }

}
