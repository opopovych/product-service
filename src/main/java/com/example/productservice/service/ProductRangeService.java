package com.example.productservice.service;

import java.io.IOException;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface ProductRangeService {
    ResponseEntity<String> uploadExcelToDb(String id, MultipartFile file) throws IOException;
    ResponseEntity<String> viewExcelFromDB(String id);
}
