package domain;

import java.math.BigDecimal;

public class Product {
    private final int id;
    private final String name;
    private final BigDecimal price;

    public Product(int id, String name, BigDecimal price) {
        if (id <= 0) {
            throw new IllegalArgumentException("Product ID must be greater than zero");
        }

    if (name == null || name.isBlank()) {
        throw new IllegalArgumentException("Product name cannot be blank");
    }

    if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
        throw new IllegalArgumentException("Product price must be greater than zero");
    }

    this.id = id;
    this.name = name;
    this.price = price;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}