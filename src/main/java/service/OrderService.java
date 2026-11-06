package service;

import domain.Customer;
import domain.Order;
import domain.Product;

public class OrderService {
    private int nextOrderId = 101;

    public Order createOrder(Customer customer) {
        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null");
        }
        return new Order(nextOrderId++, customer);
    }

    public void addProductToOrder(Order order, Product product, int quantity) {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }
        order.addItem(product, quantity);
    }
}