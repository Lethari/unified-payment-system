package app;

import java.math.BigDecimal;
import java.util.List;

import domain.Customer;
import domain.Order;
import domain.payment.PaymentMethod; // Capstone 2
import service.OrderService;
import service.PaymentMethodFactory; // Capstone 2
import service.PaymentProcessor; // Capstone 2
import service.ProductCatalog;
import summary.ReceiptPrinter;

public class PayNestApplication {
    public PayNestApplication() {
    }

    public static void main(String[] args) {
        OrderService orderService = new OrderService();
        ReceiptPrinter printer = new ReceiptPrinter();
        ProductCatalog productCatalog = new ProductCatalog();
        Customer customer = new Customer(101, "Lethabo", "lethabo@email.com");
        Order order = orderService.createOrder(customer);
        orderService.addProductToOrder(order, productCatalog.findById(1), 1);
        orderService.addProductToOrder(order, productCatalog.findById(2), 2);
        BigDecimal orderTotal = order.calculateTotal();

        // Get payment methods from factory instead of constructing directly
        List<PaymentMethod> availableMethods = PaymentMethodFactory.getAvailableMethods();
        PaymentMethod paymentMethod = availableMethods.get(0); // 0=Card, 1=EFT, 2=Wallet, 3=COD
        

        PaymentProcessor paymentProcessor = new PaymentProcessor();
        boolean paymentSuccess = paymentProcessor.processPayment(paymentMethod, orderTotal);
        
        if (paymentSuccess) {
            System.out.println("\nCheckout complete. Printing receipt...\n");
            printer.print(order);
        } else {
            System.out.println("Checkout failed. Please try another payment method.");
        }
    }
}