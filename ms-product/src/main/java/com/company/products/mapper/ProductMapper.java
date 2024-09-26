package com.company.products.mapper;

import com.company.products.dao.entity.ProductEntity;
import com.company.products.dto.ProductCreateRequest;
import com.company.products.dto.ProductResponse;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {

    ProductEntity toEntity(ProductCreateRequest request);

    ProductResponse toResponse(ProductEntity product);

}
