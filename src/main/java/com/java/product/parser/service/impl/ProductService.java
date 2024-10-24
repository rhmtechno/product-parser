package com.java.product.parser.service.impl;

import com.java.product.parser.domain.dto.ProductDto;
import com.java.product.parser.domain.entity.Product;
import com.java.product.parser.repository.ProductRepository;
import com.java.product.parser.utils.Mapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;

    public Product getProductBySku(String sku) {
        return productRepository.findBySku(sku).orElse(null);
    }

    public void saveOrUpdate(Product product) {
        productRepository.save(product);
    }

    public List<ProductDto> getAllProducts() {
        log.info("Fetching all products from the database");
        return productRepository.findAll().stream().map(Mapper::mapToDto).collect(Collectors.toList());
    }

    public Optional<ProductDto> getProductDtoBySku(String sku) {
        log.info("Fetching product with SKU: {}", sku);
        return productRepository.findBySku(sku).map(Mapper::mapToDto);
    }

}
