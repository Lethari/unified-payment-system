package service;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

import domain.Product;

public class ProductCatalog {
    private final Map<Integer, Product> products = new LinkedHashMap<>();

    public ProductCatalog() {
        products.put(1, new Product(1, "PS5", new BigDecimal("15600.00")));
        products.put(2, new Product(2, "Controller", new BigDecimal("850.00")));
    }

    public Product findById(int id) {
        Product product = products.get(id);
        if (product == null) {
            throw new IllegalArgumentException("Product not found: " + id);
        }
        return product;
    }
}
