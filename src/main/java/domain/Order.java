package domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class Order {
    private final int id;
    private final Customer customer;
    private final List<OrderItem> items;

    public Order(int id, Customer customer) {
        this.id = id;
        this.customer = customer;
        this.items = new ArrayList<>();
    }

    public void addItem(Product product, int quantity) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }

        for (OrderItem existingItem : items) {
            if (existingItem.getProduct().getId() == product.getId()) {
                existingItem.addQuantity(quantity);
                return;
            }
        }

        items.add(new OrderItem(product, quantity));
    }

    public BigDecimal calculateTotal() {
        BigDecimal total = BigDecimal.ZERO;
        for (OrderItem item : items) {
            total = total.add(item.getLineTotal());
        }
        return total.setScale(2);
    }

    public int getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public List<OrderItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Order ID: ").append(id).append("\n");
        sb.append("Customer: ").append(customer.getName()).append("\n");
        sb.append("Items:\n");
        for (OrderItem item : items) {
            sb.append(" - ").append(item.getProduct().getName())
                    .append(" x").append(item.getQuantity())
                    .append(" = ").append(formatCurrency(item.getLineTotal())).append("\n");
        }
        sb.append("Total: ").append(formatCurrency(calculateTotal()));
        return sb.toString();
    }

    private String formatCurrency(BigDecimal amount) {
        return "R" + String.format(Locale.US, "%,.2f", amount);
    }
}