package com.example.productservice.controlers;

import com.example.productservice.model.ExcelFile;
import com.example.productservice.repository.ExcelFileRepository;
import com.example.productservice.service.ExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/excel")
public class ExcelFileController {
    @Autowired
    private ExcelService excelService;

    @Autowired
    private ExcelFileRepository excelFileRepository;

    // Завантаження нового Excel файлу
    @PostMapping("/upload")
    public ResponseEntity<String> uploadExcel(@RequestParam("excelL") MultipartFile excelL,
                                              @RequestParam("excelM") MultipartFile excelM,
                                              @RequestParam("excelS") MultipartFile excelS) {
        try {
            excelService.uploadExcelToDb("L", excelL);
            excelService.uploadExcelToDb("M", excelM);
            excelService.uploadExcelToDb("S", excelS);
            return ResponseEntity.ok("Файли успішно завантажено!");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading file: " + e.getMessage());
        }
    }

    // Перегляд актуального Excel файлу
    @GetMapping("/view/{id}")
    public ResponseEntity<String> viewExcel(@PathVariable String id) {
        return excelService.viewExcelFromDB(id);
    }
    @GetMapping("/download/{id}")
    public ResponseEntity<ByteArrayResource> downloadExcel(@PathVariable String id) {
        return excelService.downloadExcel(id);
    }
}
