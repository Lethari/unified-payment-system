package summary;

import domain.Order;
import domain.OrderItem;
import money.RandFormatter;

public class ReceiptPrinter {
    public void print(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null.");
        }

        System.out.println("=================================");
        System.out.println(" PAYNEST RECEIPT");
        System.out.println("=================================");
        System.out.println("Order ID: " + order.getId());
        System.out.println("Customer: " + order.getCustomer().getName());
        System.out.println("---------------------------------");

        for (OrderItem item : order.getItems()) {
            System.out.printf("%s x%d %s%n",
                    item.getProduct().getName(),
                    item.getQuantity(),
                    RandFormatter.format(item.getLineTotal()));
        }

        System.out.println("---------------------------------");
        System.out.println("TOTAL: " + RandFormatter.format(order.calculateTotal()));
        System.out.println("=================================");
    }
}