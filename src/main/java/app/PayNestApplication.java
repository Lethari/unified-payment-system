package app;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

import domain.Customer;
import domain.Order;
import payment.PaymentMethod;
import service.OrderService;
import service.PaymentMethodFactory;
import service.PaymentProcessor;
import service.ProductCatalog;
import summary.ReceiptPrinter;

public class PayNestApplication {
    public PayNestApplication() {
    }

    public static void main(String[] args) {
        OrderService orderService = new OrderService();
        ProductCatalog productCatalog = new ProductCatalog();
        Customer customer = new Customer(101, "Lethabo", "lethabo@email.com");
        Order order = orderService.createOrder(customer);
        orderService.addProductToOrder(order, productCatalog.findById(1), 1);
        orderService.addProductToOrder(order, productCatalog.findById(2), 2);

        List<PaymentMethod> availableMethods = PaymentMethodFactory.getAvailableMethods();
        runCheckout(new Scanner(System.in), order, availableMethods);
    }

    static void runCheckout(Scanner input, Order order, List<PaymentMethod> availableMethods) {
        PaymentProcessor paymentProcessor = new PaymentProcessor();
        ReceiptPrinter printer = new ReceiptPrinter();

        while (true) {
            BigDecimal orderTotal = order.calculateTotal();
            List<PaymentMethod> eligibleMethods = availableMethods.stream()
                    .filter(method -> method.isAvailableFor(orderTotal))
                    .toList();
            PaymentMethod paymentMethod = promptForPaymentMethod(input, eligibleMethods);
            if (paymentMethod == null) {
                System.out.println("Checkout cancelled.");
                return;
            }

            if (paymentProcessor.processPayment(paymentMethod, orderTotal)) {
                System.out.println("\nCheckout complete. Printing receipt...\n");
                printer.print(order);
                return;
            }

            System.out.println("Please choose another payment method or cancel checkout.");
        }
    }

    /**
     * Prompts the user to select a payment method from the list of available methods.
     *
     * @param input           The scanner to read user input.
     * @param availableMethods The list of available payment methods.
     * COD unavailable for orders above R1000.00, so it will not be listed if the order total exceeds this amount.
     * @return The selected payment method, or null if the user cancels the checkout.
     */
    private static PaymentMethod promptForPaymentMethod(
            Scanner input, List<PaymentMethod> availableMethods) {
        while (true) {
            System.out.println("Choose a payment method:");
            for (int i = 0; i < availableMethods.size(); i++) {
                String paymentType = availableMethods.get(i).getPaymentType().replace('_', ' ');
                System.out.printf("%d) %s%n", i + 1, paymentType);
            }
            System.out.println("0) Cancel checkout");
            System.out.print("Selection: ");

            if (!input.hasNextLine()) {
                return null;
            }

            String selection = input.nextLine().trim();
            int option;
            try {
                option = Integer.parseInt(selection);
            } catch (NumberFormatException exception) {
                System.out.println("Invalid selection. Enter a listed number.");
                continue;
            }

            if (option == 0) {
                return null;
            }
            if (option >= 1 && option <= availableMethods.size()) {
                return availableMethods.get(option - 1);
            }
            System.out.println("Invalid selection. Enter a listed number.");
        }
    }
}