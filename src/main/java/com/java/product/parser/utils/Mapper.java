package com.java.product.parser.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.product.parser.domain.dto.ProductDto;
import com.java.product.parser.domain.entity.Product;

public class Mapper {
    public static ProductDto mapToDto(Product product) {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(product, ProductDto.class);
    }
}
