package com.example.productservice.service.impl;

import com.example.productservice.model.HtmlPrice;
import com.example.productservice.model.Product;
import com.example.productservice.repository.HtmlPriceRepository;
import com.example.productservice.service.ProductService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private HtmlPriceRepository repository;

}
