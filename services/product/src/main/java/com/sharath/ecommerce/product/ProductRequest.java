package com.sharath.ecommerce.product;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ProductRequest(Integer id,
                             @NotNull(message = "Product name is required")
                             String name,
                             @NotNull(message = "product description is required")
                             String description,
                             @Positive(message = "Product quantity should be positive")
                             double availableQuantity,
                             @NotNull(message = "Price is required")
                             BigDecimal price,
                             @NotNull(message = "Category is required")
                             Integer categoryId) {
}
