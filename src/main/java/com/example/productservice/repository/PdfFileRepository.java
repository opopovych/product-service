package com.example.productservice.repository;

import com.example.productservice.model.PdfFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PdfFileRepository extends JpaRepository<PdfFile, String> {
    // Отримуємо останній завантажений прайс (за датою завантаження)
    Optional<PdfFile> findTopByOrderByUploadDateDesc();
}
