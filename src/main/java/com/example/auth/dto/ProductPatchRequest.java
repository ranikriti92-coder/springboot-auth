package com.example.auth.dto;

import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Used for PATCH (partial update) — all fields optional.
 * Only non-null fields are applied to the existing record.
 */
@Data
public class ProductPatchRequest {

    private String name;

    private String description;

    @Positive(message = "Price must be positive")
    private BigDecimal price;

    private String category;

    private Integer stock;
}
