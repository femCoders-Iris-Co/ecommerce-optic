package com.irisandco.ecommerce_optic.product;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.List;

public record ProductRequest(
        @NotBlank(message = "Product name must not be empty")
        @Size(min = 2, max = 50, message = "Username must contain min 2 and max 50 characters")
        String name,
        @Positive(message = "Price must be positive")
        @DecimalMax(value = "1000.00", message = "Price must be lower than 1000")
        Double price,
        String imageUrl,
        Boolean featured,
        List<String> categoryNames
) {
}
