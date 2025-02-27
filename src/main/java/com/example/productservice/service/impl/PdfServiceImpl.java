package com.example.productservice.service.impl;

import com.example.productservice.model.PdfFile;
import com.example.productservice.repository.PdfFileRepository;
import com.example.productservice.service.PdfService;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PdfServiceImpl implements PdfService {

    @Autowired
    private PdfFileRepository pdfFileRepository;

    @Override
    public ResponseEntity<String> uploadPdfToDb(String id, MultipartFile file) throws IOException {
        byte[] pdfContent = file.getBytes();
        PdfFile pdfFile = new PdfFile();
        pdfFile.setFileData(pdfContent);
        pdfFile.setFilename(file.getOriginalFilename());
        pdfFile.setUploadDate(LocalDate.now());
        pdfFile.setId(id);
        // Зберігаємо новий файл в базі даних
        pdfFileRepository.save(pdfFile);

        return ResponseEntity.ok("PDF file uploaded successfully!");
    }

    @Override
    public ResponseEntity<byte[]> viewPdfFromDb(String id) {
        Optional<PdfFile> latestPdf = pdfFileRepository.findById(id); // Отримуємо  завантажений файл за типом

        if (latestPdf.isPresent()) {
            byte[] pdfContent = latestPdf.get().getFileData();

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + latestPdf.get().getFilename());
            headers.add(HttpHeaders.CONTENT_TYPE, "application/pdf");

            return new ResponseEntity<>(pdfContent, headers, HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Якщо файл не знайдено
        }
    }

}
