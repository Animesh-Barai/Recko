package com.example.neelanshsethi.recko.Model;

public class CarouselModel {

    public CarouselModel(String carousel_id, String image_url, String web_link) {
        this.carousel_id = carousel_id;
        this.image_url = image_url;
        this.web_link = web_link;
    }

    public String getCarousel_id() {
        return carousel_id;
    }

    public void setCarousel_id(String carousel_id) {
        this.carousel_id = carousel_id;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getWeb_link() {
        return web_link;
    }

    public void setWeb_link(String web_link) {
        this.web_link = web_link;
    }

    private String carousel_id;
    private String image_url;
    private String  web_link;

}
