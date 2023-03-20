package com.example.project_last.Model;

import com.google.common.collect.Collections2;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class RoomList {
    private String property, address, phone, location, price, id, imageUrl, category, details, new_id, key;

    public RoomList() {
    }

    public RoomList(String property, String address, String phone, String location, String price, String id, String imageUrl, String category, String details, String new_id) {
        this.property = property;
        this.address = address;
        this.phone = phone;
        this.location = location;
        this.price = price;
        this.id = id;
        this.imageUrl = imageUrl;
        this.category = category;
        this.details = details;
        this.new_id = new_id;
    }

    public static Comparator<RoomList> high_to_low = new Comparator<RoomList>() {
        @Override
        public int compare(RoomList r1, RoomList r2) {
            return r1.price.compareTo(r2.price);
        }
    };
    public static Comparator<RoomList> low_to_high= new Comparator<RoomList>() {
        @Override
        public int compare(RoomList r1, RoomList r2) {
            return r2.price.compareTo(r1.price);
        }
    };

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getNew_id() {
        return new_id;
    }

    public void setNew_id(String new_id) {
        this.new_id = new_id;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
