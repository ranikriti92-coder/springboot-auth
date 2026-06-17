package com.example.auth.service;

import com.example.auth.dto.ProductPatchRequest;
import com.example.auth.dto.ProductRequest;
import com.example.auth.dto.ProductResponse;
import com.example.auth.entity.Product;
import com.example.auth.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    // ------------------------------------------------------------------ GET

    /** GET /api/products — all authenticated users */
    @Transactional(readOnly = true)
    public List<ProductResponse> findAll() {
        return productRepository.findAll()
                .stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList());
    }

    /** GET /api/products/{id} — all authenticated users */
    @Transactional(readOnly = true)
    public ProductResponse findById(Long id) {
        return ProductResponse.from(getOrThrow(id));
    }

    /** GET /api/products/search?name=xyz — all authenticated users */
    @Transactional(readOnly = true)
    public List<ProductResponse> searchByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList());
    }

    /** GET /api/products/category/{cat} — all authenticated users */
    @Transactional(readOnly = true)
    public List<ProductResponse> findByCategory(String category) {
        return productRepository.findByCategory(category)
                .stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList());
    }

    // ------------------------------------------------------------------ POST

    /** POST /api/products — ADMIN and USER */
    @Transactional
    public ProductResponse create(ProductRequest request) {
        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setCategory(request.getCategory());
        product.setStock(request.getStock());
        return ProductResponse.from(productRepository.save(product));
    }

    // ------------------------------------------------------------------ PUT

    /** PUT /api/products/{id} — MODERATOR and ADMIN (full replace) */
    @Transactional
    public ProductResponse replace(Long id, ProductRequest request) {
        Product product = getOrThrow(id);
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setCategory(request.getCategory());
        product.setStock(request.getStock());
        return ProductResponse.from(productRepository.save(product));
    }

    // ---------------------------------------------------------------- PATCH

    /** PATCH /api/products/{id} — MODERATOR and ADMIN (partial update) */
    @Transactional
    public ProductResponse patch(Long id, ProductPatchRequest request) {
        Product product = getOrThrow(id);
        if (request.getName()        != null) product.setName(request.getName());
        if (request.getDescription() != null) product.setDescription(request.getDescription());
        if (request.getPrice()       != null) product.setPrice(request.getPrice());
        if (request.getCategory()    != null) product.setCategory(request.getCategory());
        if (request.getStock()       != null) product.setStock(request.getStock());
        return ProductResponse.from(productRepository.save(product));
    }

    // --------------------------------------------------------------- DELETE

    /** DELETE /api/products/{id} — ADMIN only */
    @Transactional
    public void delete(Long id) {
        if (!productRepository.existsById(id)) {
            throw new jakarta.persistence.EntityNotFoundException(
                    "Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }

    // --------------------------------------------------------------- helper

    private Product getOrThrow(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException(
                        "Product not found with id: " + id));
    }
}
