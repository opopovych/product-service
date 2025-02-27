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
            photoService.uploadPhoto("L", photoFileL);
            photoService.uploadPhoto("M", photoFileM);
            photoService.uploadPhoto("S", photoFileS);
            return ResponseEntity.ok("Файли успішно завантажено!");
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
