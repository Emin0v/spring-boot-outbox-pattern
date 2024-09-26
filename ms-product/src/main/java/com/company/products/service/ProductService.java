package com.company.products.service;

import com.company.products.dao.entity.ProductEntity;
import com.company.products.dao.repository.ProductRepository;
import com.company.products.dto.ProductCreateRequest;
import com.company.products.dto.ProductResponse;
import com.company.products.mapper.ProductMapper;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductMapper productMapper;
    private final ProductRepository productRepository;

    public List<ProductResponse> findAll() {
        return productRepository.findAll()
                .stream()
                .map(productMapper::toResponse)
                .collect(Collectors.toList());
    }

    public ProductResponse save(ProductCreateRequest request) {
        ProductEntity productEntity = productMapper.toEntity(request);
        productEntity = productRepository.save(productEntity);
        return productMapper.toResponse(productEntity);
    }

}

