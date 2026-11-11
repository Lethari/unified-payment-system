package app;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import domain.Customer;
import domain.Order;
import domain.Product;
import payment.CashOnDelivery;
import payment.PaymentMethod;

class PayNestApplicationTest {
    // Test to ensure that when a payment is declined, the user is returned to the menu and can select another payment method.

    @Test
    void declinedPaymentReturnsToMenuAndAllowsAnotherMethod() {
        AtomicInteger declinedAttempts = new AtomicInteger();
        PaymentMethod decliningMethod = new PaymentMethod() {
            @Override
            public boolean processPayment(BigDecimal amount) {
                declinedAttempts.incrementAndGet();
                return false;
            }

            @Override
            public String getPaymentType() {
                return "DECLINED";
            }
        };
        PaymentMethod succeedingMethod = new PaymentMethod() {
            @Override
            public boolean processPayment(BigDecimal amount) {
                assertEquals(new BigDecimal("25.00"), amount);
                return true;
            }

            @Override
            public String getPaymentType() {
                return "TEST";
            }
        };
        Order order = createOrder();
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        runCheckout("1\n2\n", order, List.of(decliningMethod, succeedingMethod), output);

        assertEquals(1, declinedAttempts.get());
        assertTrue(output.toString().contains("Payment failed via DECLINED"));
        assertTrue(output.toString().contains("Payment successful via TEST"));
        assertTrue(output.toString().contains("PAYNEST RECEIPT"));
    }

    @Test
    void invalidSelectionIsRejectedAndCustomerCanCancel() {
        AtomicInteger paymentAttempts = new AtomicInteger();
        PaymentMethod method = new PaymentMethod() {
            @Override
            public boolean processPayment(BigDecimal amount) {
                paymentAttempts.incrementAndGet();
                return true;
            }

            @Override
            public String getPaymentType() {
                return "TEST";
            }
        };
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        runCheckout("not a number\n0\n", createOrder(), List.of(method), output);

        assertEquals(0, paymentAttempts.get());
        assertTrue(output.toString().contains("Invalid selection."));
        assertTrue(output.toString().contains("Checkout cancelled."));
    }

    @Test
    void cashOnDeliveryIsNotOfferedWhenOrderTotalExceedsOneThousand() {
        PaymentMethod otherMethod = new PaymentMethod() {
            @Override
            public boolean processPayment(BigDecimal amount) {
                return true;
            }

            @Override
            public String getPaymentType() {
                return "TEST";
            }
        };
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        runCheckout("2\n0\n", createOrder("1000.01"),
                List.of(new CashOnDelivery(), otherMethod), output);

        assertTrue(output.toString().contains("1) TEST"));
        assertFalse(output.toString().contains("Cash on Delivery"));
        assertTrue(output.toString().contains("Invalid selection."));
        assertTrue(output.toString().contains("Checkout cancelled."));
    }

    @Test
    void cashOnDeliveryIsOfferedWhenOrderTotalIsOneThousand() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        runCheckout("1\n", createOrder("1000.00"),
                List.of(new CashOnDelivery()), output);

        assertTrue(output.toString().contains("1) Cash on Delivery"));
        assertTrue(output.toString().contains("Amount to be collected: R1,000.00"));
    }

    private void runCheckout(
            String selections, Order order, List<PaymentMethod> methods, ByteArrayOutputStream output) {
        PrintStream originalOut = System.out;
        try (Scanner input = new Scanner(
                new ByteArrayInputStream(selections.getBytes(StandardCharsets.UTF_8)));
                PrintStream capturedOut = new PrintStream(output, true, StandardCharsets.UTF_8)) {
            System.setOut(capturedOut);
            PayNestApplication.runCheckout(input, order, methods);
        } finally {
            System.setOut(originalOut);
        }
    }

    private Order createOrder() {
        return createOrder("25.00");
    }

    private Order createOrder(String price) {
        Order order = new Order(1, new Customer(101, "Lethabo", "lethabo@email.com"));
        order.addItem(new Product(1, "Item", new BigDecimal(price)), 1);
        return order;
    }
}
