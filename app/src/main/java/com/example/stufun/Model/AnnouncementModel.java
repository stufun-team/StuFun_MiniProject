package com.example.stufun.Model;

public class AnnouncementModel {

    String id,text;

    public AnnouncementModel(String id) {
        this.id = id;
    }

    public AnnouncementModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
