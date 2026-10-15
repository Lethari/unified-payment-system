package domain;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import domain.payment.CardPayment;
import domain.payment.CashOnDelivery;
import domain.payment.EftPayment;
import domain.payment.PaymentMethod;
import domain.payment.WalletPayment;
import service.OrderService;
import service.PaymentProcessor;
import summary.ReceiptPrinter;

@SuppressWarnings("ThrowableResultIgnored")
class OrderTest {

    @Test
    void calculatesTotalForMultipleItems() {
        Customer customer = new Customer(101, "Test", "test@example.com");
        Order order = new Order(1, customer);

        order.addItem(new Product(1, "PS5", new BigDecimal("15600.00")), 1);
        order.addItem(new Product(2, "Controller", new BigDecimal("850.00")), 2);

        assertEquals(new BigDecimal("17300.00"), order.calculateTotal());
        assertEquals(2, order.getItems().size());
    }

    @Test
    void rejectsNullProductAndInvalidQuantity() {
        Customer customer = new Customer(101, "Test", "test@example.com");
        Order order = new Order(2, customer);
        Product product = new Product(1, "Keyboard", new BigDecimal("250.00"));

        Throwable firstThrown = assertThrows(IllegalArgumentException.class, () -> order.addItem(null, 1));
        Throwable secondThrown = assertThrows(IllegalArgumentException.class, () -> order.addItem(product, 0));
        Throwable thirdThrown = assertThrows(IllegalArgumentException.class, () -> order.addItem(product, -1));
        assertTrue(firstThrown instanceof IllegalArgumentException);
        assertTrue(secondThrown instanceof IllegalArgumentException);
        assertTrue(thirdThrown instanceof IllegalArgumentException);
    }

    @Test
    void aggregatesDuplicateProductsIntoOneLine() {
        Customer customer = new Customer(101, "Test", "test@example.com");
        Order order = new Order(3, customer);
        Product product = new Product(1, "Mouse", new BigDecimal("50.00"));

        order.addItem(product, 1);
        order.addItem(product, 2);

        assertEquals(1, order.getItems().size());
        assertEquals(3, order.getItems().get(0).getQuantity());
        assertEquals(new BigDecimal("150.00"), order.calculateTotal());
    }

    @Test
    void emptyOrderHasZeroTotalAndImmutableItemsView() {
        Customer customer = new Customer(101, "Test", "test@example.com");
        Order order = new Order(4, customer);

        assertEquals(new BigDecimal("0.00"), order.calculateTotal());
        assertTrue(order.getItems().isEmpty());
        Throwable thrown = assertThrows(UnsupportedOperationException.class,
                () -> order.getItems().add(new OrderItem(new Product(1, "Test", new BigDecimal("10.00")), 1)));
        assertTrue(thrown instanceof UnsupportedOperationException);
    }

    @Test
    void receiptPrinterFormatsOrderSummary() {
        OrderService orderService = new OrderService();
        ReceiptPrinter printer = new ReceiptPrinter();
        Customer customer = new Customer(101, "Lethabo", "lethabo@email.com");
        Order order = orderService.createOrder(customer);

        orderService.addProductToOrder(order, new Product(1, "PS5", new BigDecimal("15600.00")), 1);
        orderService.addProductToOrder(order, new Product(2, "Controller", new BigDecimal("850.00")), 2);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        try {
            System.setOut(new PrintStream(outputStream));
            printer.print(order);
        } finally {
            System.setOut(originalOut);
        }

        String receipt = outputStream.toString();
        assertTrue(receipt.contains("PAYNEST RECEIPT"));
        assertTrue(receipt.contains("TOTAL: R17,300.00"));
        assertTrue(receipt.contains("PS5 x1"));
        assertTrue(receipt.contains("Controller x2"));
    }

    @Test
    void cardPaymentUsesPlainAsciiCurrencyFormatting() {
        CardPayment card = new CardPayment("4242424242424242", "Lethabo", "12/26", "123");

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        try {
            System.setOut(new PrintStream(outputStream));
            card.processPayment(new BigDecimal("17300.00"));
        } finally {
            System.setOut(originalOut);
        }

        String output = outputStream.toString();
        assertTrue(output.contains("Processing Card payment of R17,300.00"));
        assertTrue(!output.contains("R17\u00A0300.00"));
    }

    @Test
    void checkoutCanUseDifferentPaymentMethods() {
        PaymentMethod card = new CardPayment("4242424242424242", "Test User", "12/26", "123");
        PaymentMethod wallet = new WalletPayment("Zapper", "WALLET123456");
        PaymentMethod cash = new CashOnDelivery();
        PaymentMethod eft = new EftPayment("1234567890", "Test User", "000123");

        assertTrue(card.processPayment(new BigDecimal("12400.00")));
        assertTrue(wallet.processPayment(new BigDecimal("12400.00")));
        assertEquals("CARD", card.getPaymentType());
        assertEquals("WALLET", wallet.getPaymentType());
        assertEquals("CASH_ON_DELIVERY", cash.getPaymentType());
        assertEquals("EFT", eft.getPaymentType());
    }

    @Test
    void checkoutUsesOrderTotalAndDifferentPaymentMethods() {
        Customer customer = new Customer(101, "Test", "test@example.com");
        Order order = new Order(5, customer);
        order.addItem(new Product(1, "PS5", new BigDecimal("15600.00")), 1);
        order.addItem(new Product(2, "Controller", new BigDecimal("850.00")), 2);

        BigDecimal orderTotal = order.calculateTotal();
        PaymentMethod card = new CardPayment("4242424242424242", "Test User", "12/26", "123");
        PaymentMethod wallet = new WalletPayment("Zapper", "WALLET123456");
        PaymentMethod cash = new CashOnDelivery();
        PaymentMethod eft = new EftPayment("1234567890", "Test User", "000123");
        PaymentProcessor processor = new PaymentProcessor();

        assertTrue(processor.processPayment(card, orderTotal));
        assertTrue(processor.processPayment(wallet, orderTotal));
        assertTrue(processor.processPayment(cash, orderTotal));
        assertTrue(processor.processPayment(eft, orderTotal));
    }
}
