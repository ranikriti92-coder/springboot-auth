package com.example.auth.controller;

import com.example.auth.dto.MessageResponse;
import com.example.auth.dto.ProductPatchRequest;
import com.example.auth.dto.ProductRequest;
import com.example.auth.dto.ProductResponse;
import com.example.auth.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Product CRUD REST API
 *
 * Role matrix:
 * ┌──────────┬──────────┬─────────────────────────────────────────────┐
 * │ Method   │ Endpoint              │ Allowed Roles                  │
 * ├──────────┼───────────────────────┼────────────────────────────────┤
 * │ GET      │ /api/products         │ USER, MODERATOR, ADMIN         │
 * │ GET      │ /api/products/{id}    │ USER, MODERATOR, ADMIN         │
 * │ GET      │ /api/products/search  │ USER, MODERATOR, ADMIN         │
 * │ GET      │ /api/products/category│ USER, MODERATOR, ADMIN         │
 * │ POST     │ /api/products         │ USER, ADMIN                    │
 * │ PUT      │ /api/products/{id}    │ MODERATOR, ADMIN               │
 * │ PATCH    │ /api/products/{id}    │ MODERATOR, ADMIN               │
 * │ DELETE   │ /api/products/{id}    │ ADMIN only                     │
 * └──────────┴───────────────────────┴────────────────────────────────┘
 */
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // ===================================================================
    //  GET — all authenticated users (USER + MODERATOR + ADMIN)
    // ===================================================================

    /**
     * GET /api/products
     * Returns all products.
     */
    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        return ResponseEntity.ok(productService.findAll());
    }

    /**
     * GET /api/products/{id}
     * Returns a single product by ID.
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.findById(id));
    }

    /**
     * GET /api/products/search?name=laptop
     * Search products by partial name (case-insensitive).
     */
    @GetMapping("/search")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<List<ProductResponse>> searchProducts(
            @RequestParam String name) {
        return ResponseEntity.ok(productService.searchByName(name));
    }

    /**
     * GET /api/products/category/{category}
     * Filter products by exact category name.
     */
    @GetMapping("/category/{category}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<List<ProductResponse>> getByCategory(
            @PathVariable String category) {
        return ResponseEntity.ok(productService.findByCategory(category));
    }

    // ===================================================================
    //  POST — USER and ADMIN (moderators cannot create)
    // ===================================================================

    /**
     * POST /api/products
     * Create a new product.
     *
     * Body: { "name": "Laptop", "description": "...", "price": 999.99,
     *         "category": "Electronics", "stock": 50 }
     */
    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ProductResponse> createProduct(
            @Valid @RequestBody ProductRequest request) {
        ProductResponse created = productService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // ===================================================================
    //  PUT — MODERATOR and ADMIN (full replacement, all fields required)
    // ===================================================================

    /**
     * PUT /api/products/{id}
     * Fully replace an existing product (all fields must be supplied).
     *
     * Body: { "name": "Gaming Laptop", "description": "...",
     *         "price": 1299.99, "category": "Electronics", "stock": 30 }
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<ProductResponse> replaceProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequest request) {
        return ResponseEntity.ok(productService.replace(id, request));
    }

    // ===================================================================
    //  PATCH — MODERATOR and ADMIN (partial update, only supplied fields)
    // ===================================================================

    /**
     * PATCH /api/products/{id}
     * Partially update a product — only the fields you send are changed.
     *
     * Body (any subset): { "price": 899.99, "stock": 45 }
     */
    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<ProductResponse> patchProduct(
            @PathVariable Long id,
            @RequestBody ProductPatchRequest request) {
        return ResponseEntity.ok(productService.patch(id, request));
    }

    // ===================================================================
    //  DELETE — ADMIN only
    // ===================================================================

    /**
     * DELETE /api/products/{id}
     * Permanently delete a product. Admin only.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageResponse> deleteProduct(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.ok(new MessageResponse(
                "Product with id " + id + " deleted successfully."));
    }
}
