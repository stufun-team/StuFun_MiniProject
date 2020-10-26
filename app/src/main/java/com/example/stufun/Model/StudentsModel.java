package com.example.stufun.Model;

public class StudentsModel {
    String name,organization,image,email;

    public StudentsModel() {
    }

    public StudentsModel(String name, String organization, String image, String email) {
        this.name = name;
        this.organization = organization;
        this.image = image;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
