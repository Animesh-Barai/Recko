package com.recko.app.Model;

public class VideosModel {

    public VideosModel(String thumbnail_url, String video_url, String video_uuid, String title) {
        this.thumbnail_url = thumbnail_url;
        this.video_url = video_url;
        this.video_uuid = video_uuid;
        this.title = title;
    }

    public String getThumbnail_url() {
        return thumbnail_url;
    }

    public void setThumbnail_url(String thumbnail_url) {
        this.thumbnail_url = thumbnail_url;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public String getVideo_uuid() {
        return video_uuid;
    }

    public void setVideo_uuid(String video_uuid) {
        this.video_uuid = video_uuid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private String thumbnail_url;
    private String video_url;
    private String video_uuid;
    private String title;

    public String getFlavour_text() {
        return flavour_text;
    }

    public void setFlavour_text(String flavour_text) {
        if (flavour_text.equals("null")) flavour_text = "";
        flavour_text = flavour_text.replace("<br/>", "\n");
        this.flavour_text = flavour_text;
    }

    private String flavour_text;
}
