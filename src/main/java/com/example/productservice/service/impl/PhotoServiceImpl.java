package com.example.productservice.service.impl;

import com.example.productservice.model.Photo;
import com.example.productservice.repository.PhotoRepository;
import com.example.productservice.service.PhotoService;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PhotoServiceImpl implements PhotoService {
    @Autowired
    private PhotoRepository photoRepository;

    @Override
    public ResponseEntity<?> uploadPhoto(String id, MultipartFile photoFile) throws IOException {
        Photo photo = new Photo();
        photo.setId(id);
        photo.setFilename(photoFile.getOriginalFilename());
        photo.setData(photoFile.getBytes());
        photo.setUploadDate(LocalDate.now());
        photoRepository.save(photo);
        return ResponseEntity.ok(photo);
    }

    @Override
    public ResponseEntity<byte[]> getPhoto(String id) {
        Optional<Photo> photoOptional = photoRepository.findById(id);
        if (!photoOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Photo photo = photoOptional.get();
        return new ResponseEntity<>(photo.getData(), HttpStatus.OK);
    }
}
