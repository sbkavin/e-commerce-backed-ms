package com.sharath.ecommerce.product;

import com.sharath.ecommerce.category.Category;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class ProductMapper {

    public Product toProduct(ProductRequest request) {

        return Product.builder()
                .id(request.id())
                .name(request.name())
                .description(request.description())
                .availableQuantity(request.availableQuantity())
                .price(request.price())
                .category(Category.builder()
                        .id(request.categoryId())
                        .build())
                .build();
    }


    public ProductResponse toProductResponse(Product p) {
        return new ProductResponse(p.getId(), p.getName(), p.getDescription(), p.getAvailableQuantity(), p.getPrice(), p.getCategory().getId(), p.getCategory().getName(), p.getCategory().getDescription());
    }

    public ProductPurchaseResponse toproductPurchaseResponse(Product product,  double quantity) {
        return new ProductPurchaseResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                quantity
        );
    }
}
