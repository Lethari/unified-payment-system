package summary;

import java.math.BigDecimal;
import java.util.Locale;

import domain.Order;
import domain.OrderItem;

public class ReceiptPrinter {

    public void print(Order order) {
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
                    formatCurrency(item.getLineTotal()));
        }

        System.out.println("---------------------------------");
        System.out.println("TOTAL: " + formatCurrency(order.calculateTotal()));
        System.out.println("=================================");
    }

    private String formatCurrency(BigDecimal amount) {
        return "R" + String.format(Locale.US, "%,.2f", amount);
    }
}