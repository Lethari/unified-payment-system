package domain;

import java.math.BigDecimal;
import java.util.Objects;

public class Product {
    private final int id;
    private final String name;
    private final BigDecimal price;

    public Product(int id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = Objects.requireNonNull(price, "Price cannot be null");
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