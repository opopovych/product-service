package com.example.productservice.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class CategoryProducts {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String categoryName;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Product> products = new ArrayList<>();

    public CategoryProducts(String categoryName) {
        this.categoryName = categoryName;
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}