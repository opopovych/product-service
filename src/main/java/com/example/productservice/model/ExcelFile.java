package com.example.productservice.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
public class ExcelFile {

    @Id
    private String id; // "L", "M", "S"

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] fileData;

    private String filename;


    @Column(name = "upload_date")
    private LocalDate uploadDate;

    // Геттери та сеттери
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public byte[] getFileData() {
        return fileData;
    }

    public void setFileData(byte[] fileData) {
        this.fileData = fileData;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
    public LocalDate getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(LocalDate uploadDate) {
        this.uploadDate = uploadDate;
    }


}


