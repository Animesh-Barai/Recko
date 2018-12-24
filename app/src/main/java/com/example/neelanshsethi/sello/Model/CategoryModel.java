package com.example.neelanshsethi.sello.Model;

import java.util.ArrayList;
import java.util.List;

public class CategoryModel {

    private List<String> categoryimageurl= new ArrayList<String >();
    private List<String> categoryname= new ArrayList<String >();
    private List<String> categoryindustry= new ArrayList<String >();
    private List<String> categorymaxcommission= new ArrayList<String >();
    private List<String> categoryuuid= new ArrayList<String >();
    private List<String> categorynoofproduct= new ArrayList<String >();

    public CategoryModel(List<String> categoryimageurl, List<String> categoryname, List<String> categoryindustry, List<String> categorymaxcommission, List<String> categoryuuid, List<String> categorynoofproduct) {
        this.categoryimageurl = categoryimageurl;
        this.categoryname = categoryname;
        this.categoryindustry = categoryindustry;
        this.categorymaxcommission = categorymaxcommission;
        this.categoryuuid = categoryuuid;
        this.categorynoofproduct = categorynoofproduct;
    }

    public List<String> getCategoryimageurl() {
        return categoryimageurl;
    }

    public void setCategoryimageurl(List<String> categoryimageurl) {
        this.categoryimageurl = categoryimageurl;
    }

    public List<String> getCategoryname() {
        return categoryname;
    }

    public void setCategoryname(List<String> categoryname) {
        this.categoryname = categoryname;
    }

    public List<String> getCategoryindustry() {
        return categoryindustry;
    }

    public void setCategoryindustry(List<String> categoryindustry) {
        this.categoryindustry = categoryindustry;
    }

    public List<String> getCategorymaxcommission() {
        return categorymaxcommission;
    }

    public void setCategorymaxcommission(List<String> categorymaxcommission) {
        this.categorymaxcommission = categorymaxcommission;
    }

    public List<String> getCategoryuuid() {
        return categoryuuid;
    }

    public void setCategoryuuid(List<String> categoryuuid) {
        this.categoryuuid = categoryuuid;
    }

    public List<String> getCategorynoofproduct() {
        return categorynoofproduct;
    }

    public void setCategorynoofproduct(List<String> categorynoofproduct) {
        this.categorynoofproduct = categorynoofproduct;
    }



}