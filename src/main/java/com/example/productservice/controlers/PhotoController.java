package com.example.productservice.controlers;

import com.example.productservice.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@RestController
@RequestMapping("/photo")
public class PhotoController {
    @Autowired
    private PhotoService photoService;

    // Завантаження фото
    @PostMapping("/upload")
    public ResponseEntity<?> uploadPhoto(@RequestParam("photoL") MultipartFile photoFileL,
                                         @RequestParam("photoM") MultipartFile photoFileM,
                                         @RequestParam("photoS") MultipartFile photoFileS) {
        try {
            if (photoFileL != null && !photoFileL.isEmpty()) {
                photoService.uploadPhoto("L", photoFileL);
            }
            if (photoFileM != null && !photoFileM.isEmpty()) {
                photoService.uploadPhoto("M", photoFileM);
            }
            if (photoFileS != null && !photoFileS.isEmpty()) {
                photoService.uploadPhoto("S", photoFileS);
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
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Помилка при завантаженні файлу");
        }
    }

    // Отримання фото за ID (для відображення)
    @GetMapping("/view/{id}")
    public ResponseEntity<byte[]> getPhoto(@PathVariable String id) {

        return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(photoService.getPhoto(id).getBody());
    }
}
