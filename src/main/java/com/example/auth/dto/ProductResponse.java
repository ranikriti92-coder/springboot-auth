package com.example.auth.dto;

import com.example.auth.entity.Product;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Outbound DTO — controls exactly what fields the API exposes.
 */
@Data
public class ProductResponse {

    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String category;
    private Integer stock;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ProductResponse from(Product p) {
        ProductResponse r = new ProductResponse();
        r.id          = p.getId();
        r.name        = p.getName();
        r.description = p.getDescription();
        r.price       = p.getPrice();
        r.category    = p.getCategory();
        r.stock       = p.getStock();
        r.createdAt   = p.getCreatedAt();
        r.updatedAt   = p.getUpdatedAt();
        return r;
    }
}
