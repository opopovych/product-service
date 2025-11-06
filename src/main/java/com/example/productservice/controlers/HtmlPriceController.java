package com.example.productservice.controlers;

import com.example.productservice.model.HtmlPrice;
import com.example.productservice.model.PdfFile;
import com.example.productservice.repository.HtmlPriceRepository;
import com.example.productservice.service.impl.HtmlServiceImpl;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/html")
public class HtmlPriceController {

    @Autowired
    private HtmlServiceImpl htmlService;
    @Autowired
    private HtmlPriceRepository htmlPriceRepository;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadPdf(@RequestParam("fileL") MultipartFile fileL,
                                            @RequestParam("fileM") MultipartFile fileM,
                                            @RequestParam("fileS") MultipartFile fileS){
        try {
            if (fileL != null && !fileL.isEmpty()) {
                htmlService.uploadHtmlToDb("L", fileL);
            }

            if (fileM != null && !fileM.isEmpty()) {
                htmlService.uploadHtmlToDb("M", fileM);
            }

            if (fileS != null && !fileS.isEmpty()) {
                htmlService.uploadHtmlToDb("S", fileS);
            }
            // HTML із модальним вікном
            String html = """
    <!DOCTYPE html>
    <html lang="uk">
    <head>
        <meta charset="UTF-8">
        <title>Завантаження</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
        <style>
            .custom-modal {
                border-radius: 15px;
                border: none;
                overflow: hidden;
                box-shadow: 0 0 15px rgba(0,0,0,0.3);
            }
            .custom-modal .modal-header {
                background: linear-gradient(to right, #6F4E37, #3E2723);
                color: white;
                border: none;
            }
            .custom-modal .modal-body {
                font-size: 1.1em;
                color: #3E2723;
                padding: 20px;
                text-align: center;
            }
            .btn-coffee {
                background-color: #6F4E37;
                color: #fff;
                font-weight: bold;
                border-radius: 25px;
                padding: 10px 25px;
                border: none;
                transition: 0.3s;
            }
            .btn-coffee:hover {
                background-color: #4B3621;
                transform: scale(1.05);
            }
        </style>
    </head>
    <body>
    <div class="modal fade show" id="successModal" tabindex="-1" style="display:block;" aria-modal="true" role="dialog">
        <div class="modal-dialog">
            <div class="modal-content custom-modal">
                <div class="modal-header">
                    <h5 class="modal-title">✅ Успіх</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    ✅ Файли успішно завантажено!
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn-coffee" data-bs-dismiss="modal">Добре ☕</button>
                </div>
            </div>
        </div>
    </div>
    
    <script>
        const modalEl = document.getElementById('successModal');
        const modal = new bootstrap.Modal(modalEl);
        modal.show();

        // Коли модалка закривається -> робимо редірект
        modalEl.addEventListener('hidden.bs.modal', function () {
            window.location.href = '/index';
        });
    </script>
    </body>
    </html>
    """;

            return ResponseEntity.ok()
                    .contentType(org.springframework.http.MediaType.TEXT_HTML)
                    .body(html);

        } catch (IOException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading file: " + e.getMessage());
    }
    }
    @GetMapping("/view/{id}")
    public ResponseEntity<String> viewHtml(@PathVariable String id) {
        return htmlService.viewHtmlFromDb(id);
    }
}
