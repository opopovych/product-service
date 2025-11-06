package com.example.productservice.service;

import java.io.IOException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface HtmlService {

    ResponseEntity<String> uploadHtmlToDb(String id, MultipartFile photoFile) throws IOException;
    ResponseEntity<String> viewHtmlFromDb(String id);
}
