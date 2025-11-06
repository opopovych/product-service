package com.example.productservice.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "html_prices")
public class HtmlPrice {

    @Id
    private String id; // "L", "M", "S"

    private String name;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String content;

    private LocalDate uploadDate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDate getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(LocalDate uploadDate) {
        this.uploadDate = uploadDate;
    }
}
