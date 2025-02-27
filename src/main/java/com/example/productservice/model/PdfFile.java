package com.example.productservice.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

@Entity
public class PdfFile {

    @Id
    private String id; // "L", "M", "S"

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] fileData;  // Зберігається сам файл

    private String filename;   // Ім'я файлу
    private String contentType;
    @Column(name = "upload_date")
    private LocalDate uploadDate;

    // Гетери та сетери
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
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

    @Override
    public String toString() {
        return "PdfFile{" +
                "id='" + id + '\'' +
                ", fileData=" + Arrays.toString(fileData) +
                ", filename='" + filename + '\'' +
                ", contentType='" + contentType + '\'' +
                ", uploadDate=" + uploadDate +
                '}';
    }
}
