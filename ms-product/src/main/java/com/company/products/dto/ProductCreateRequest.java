package com.company.products.dto;

import java.math.BigDecimal;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductCreateRequest {

    private Long sellerId;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    private BigDecimal price;

    @Min(value = 1)
    private int stockQuantity;
}
