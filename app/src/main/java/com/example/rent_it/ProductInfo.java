package com.example.rent_it;

import java.io.Serializable;

public class ProductInfo implements Serializable {
    String name,price,description,duration,imageUrl,status,soldTo,ownerId;

    public ProductInfo() {
    }

    public ProductInfo(String name, String price, String description, String duration, String imageUrl, String status,String soldTo,String ownerId) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.duration = duration;
        this.imageUrl = imageUrl;
        this.status = status;
        this.soldTo=soldTo;
        this.ownerId=ownerId;
    }

    public String getOwnerId() {
        return ownerId;
    }
    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getSoldTo() {
        return soldTo;
    }

    public void setSoldTo(String soldTo) {
        this.soldTo = soldTo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
