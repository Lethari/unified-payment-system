package payment;

import java.math.BigDecimal;
import java.util.Locale;

public class CashOnDelivery implements PaymentMethod {
    // Cash on Delivery is available for orders up to R1000.00. If the order total exceeds this amount, the payment method will not be available, and the user will be informed accordingly.
    private static final BigDecimal MAX_ORDER_TOTAL = new BigDecimal("1000.00");

    @Override
    public boolean processPayment(BigDecimal amount) {
        validateAmount(amount);
        if (!isAvailableFor(amount)) {
            System.out.println("Cash on Delivery is unavailable for amounts above R"
                    + String.format(Locale.US, "%,.2f", MAX_ORDER_TOTAL));
            return false;
        }

        System.out.println("Order placed with Cash on Delivery.");
        System.out.println("Amount to be collected: " + formatCurrency(amount));
        return true;
    }

    @Override
    public boolean isAvailableFor(BigDecimal amount) {
        return amount != null
                && amount.compareTo(BigDecimal.ZERO) > 0
                && amount.compareTo(MAX_ORDER_TOTAL) <= 0;
    }

    @Override
    public String getPaymentType() {
        return "Cash on Delivery";
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
    }

    private String formatCurrency(BigDecimal amount) {
        return "R" + String.format(Locale.US, "%,.2f", amount);
    }
}