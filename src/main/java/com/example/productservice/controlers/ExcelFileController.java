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
            if (excelL != null && !excelL.isEmpty()) {
                excelService.uploadExcelToDb("L", excelL);
            }
            if (excelM != null && !excelM.isEmpty()) {
                excelService.uploadExcelToDb("M", excelM);
            }
            if (excelS != null && !excelS.isEmpty()) {
                excelService.uploadExcelToDb("S", excelS);
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
