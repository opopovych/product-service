package com.example.productservice.service;

import com.example.productservice.model.CategoryProducts;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface CatalogService {
    String convertExcelToHtmlForCatalog(byte[] fileData, String fileName);
    List<CategoryProducts> parseExcelToCategoryProductsForCatalog(byte[] fileData);
}
