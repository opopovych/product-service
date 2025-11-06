package com.example.productservice.repository;

import com.example.productservice.model.HtmlPrice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HtmlPriceRepository extends JpaRepository<HtmlPrice, String> {
}
