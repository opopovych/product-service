package com.example.productservice.service;

import java.io.IOException;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

public interface ExcelService {
    ResponseEntity<String> uploadExcelToDb(String id, MultipartFile file) throws IOException;
    ResponseEntity<String> viewExcelFromDB(String id);
    ResponseEntity<ByteArrayResource> downloadExcel(String id);

}
