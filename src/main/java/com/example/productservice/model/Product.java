package com.example.productservice.model;

import jakarta.persistence.*;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String code;
    private String name;
    private double price;
    private String imageUrl; // або base64, якщо зображення вбудоване

    public Product(String code, String name, double price, String imageUrl) {
        this.code = code;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public Product() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "Product{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
