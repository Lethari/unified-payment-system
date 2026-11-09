package service;

import domain.Customer;
import domain.Order;
import domain.Product;

public class OrderService {
    private int nextOrderId = 101;

    public Order createOrder(Customer customer) {
        return new Order(nextOrderId++, customer);
    }

    public void addProductToOrder(Order order, Product product, int quantity) {
        order.addItem(product, quantity);
    }
}