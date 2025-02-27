package com.example.productservice.controlers;

import com.example.productservice.model.PdfFile;
import com.example.productservice.repository.PdfFileRepository;
import com.example.productservice.service.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/pdf")
public class PdfFileController {

    @Autowired
    private PdfService pdfService;
    @Autowired
    private PdfFileRepository pdfFileRepository;

    // Завантаження нового PDF прайса
    @PostMapping("/upload")
    public ResponseEntity<String> uploadPdf(@RequestParam("fileL") MultipartFile fileL,
                                            @RequestParam("fileM") MultipartFile fileM,
                                            @RequestParam("fileS") MultipartFile fileS) {
        try {
            pdfService.uploadPdfToDb("L", fileL);
            pdfService.uploadPdfToDb("M", fileM);
            pdfService.uploadPdfToDb("S", fileS);
            return ResponseEntity.ok("Файли успішно завантажено!");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading file: " + e.getMessage());
        }
    }

    // Перегляд актуального PDF прайса
    @GetMapping("/view/{id}")
    public ResponseEntity<byte[]> viewPdf(@PathVariable String id) {
        PdfFile pdfFile = pdfFileRepository.findById(id).orElseThrow(() -> new RuntimeException("File not found"));
        LocalDate time = pdfFileRepository.findById(id).get().getUploadDate();
        System.out.println(time.toString());
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfFile.getFileData());
    }


    @GetMapping("/upload/list")
    public String listFiles(Model model) {
        List<PdfFile> pdfFiles = pdfFileRepository.findAll();
        model.addAttribute("file", pdfFiles);  // Додаємо список файлів в модель
        return "upload";  // Вказуємо шаблон для рендерингу (наприклад, files.html)
    }

}
