package com.bux.trading.bot.repository;

import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Dummy repository to avoid using Spring Boot Data
 */
@Component
public class ProductRepository {

    private Product product = null;

    public Product save(Product entity) {
        product = entity;
        return entity;
    }

    public Optional<Product> findById(String s) {
        if (product == null || !product.getProductId().equals(s)) {
            return Optional.empty();
        }
        return Optional.of(product);
    }

    public void delete(Product product) {
        if (this.product.getProductId().equals(product.getProductId())) {
            this.product = null;
        }
    }
}
