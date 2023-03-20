package com.example.project_last.Model;

public class User {

    private  String id;
    private  String name;
    private  String imageURL;
    private  String phone,email;


    public User(String id, String name, String imageURL,String phone,String email) {
        this.id = id;
        this.name = name;
        this.imageURL = imageURL;
        this.phone = phone;
        this.email=email;
    }
    public  User(){

    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
