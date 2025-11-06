package com.example.productservice.service.impl;

import com.example.productservice.model.HtmlPrice;
import com.example.productservice.repository.HtmlPriceRepository;
import com.example.productservice.service.HtmlService;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class HtmlServiceImpl implements HtmlService {
    @Autowired
    private HtmlPriceRepository repository;
    @Override
    public ResponseEntity<String> uploadHtmlToDb(String id, MultipartFile file) throws IOException {
        String content = new String(file.getBytes(), StandardCharsets.UTF_8);
        String styleBlock = """
    <style>
        html {
            margin: 0;
            display: flex;
            justify-content: center;
        }
    </style>
    """;

// Вставити перед </head>, якщо є
        if (content.contains("</head>")) {
            content = content.replace("</head>", styleBlock + "\n</head>");
        } else {
            // fallback — просто додати на початок, якщо head відсутній
            content = styleBlock + content;
        }
        HtmlPrice htmlPrice = new HtmlPrice();
        htmlPrice.setName(file.getOriginalFilename());
        htmlPrice.setContent(content);
        htmlPrice.setUploadDate(LocalDate.now());
        htmlPrice.setId(id);
        // Зберігаємо новий файл в базі даних
        repository.save(htmlPrice);
        //System.out.println(htmlPrice.getUploadDate().toString());
        return ResponseEntity.ok("HTML прайс завантажено.");
    }

    @Override
    public ResponseEntity<String> viewHtmlFromDb(String id) {
        Optional<HtmlPrice> htmlFile = repository.findById(id);
        //System.out.println(htmlFile.get().getContent());
        if (htmlFile.isPresent()) {
            return ResponseEntity
                    .ok()
                    .header(HttpHeaders.CONTENT_TYPE, "text/html; charset=UTF-8")
                    .body(htmlFile.get().getContent());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("<h2>HTML файл не знайдено</h2>");
        }
    }
}
