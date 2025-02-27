package com.example.productservice.service;

import java.io.IOException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface PdfService {
    ResponseEntity<String> uploadPdfToDb(String id, MultipartFile photoFile) throws IOException;
    ResponseEntity<byte[]> viewPdfFromDb(String id);
}
