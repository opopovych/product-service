package com.example.productservice.service;

import java.io.IOException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface PhotoService {
    ResponseEntity<?> uploadPhoto(String id, MultipartFile file) throws IOException;
    ResponseEntity<byte[]> getPhoto(String id);
}
