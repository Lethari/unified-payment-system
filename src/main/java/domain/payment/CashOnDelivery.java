package domain.payment;

import java.math.BigDecimal;
import java.util.Locale;

public class CashOnDelivery implements PaymentMethod {

    @Override
    public boolean processPayment(BigDecimal amount) {
        validateAmount(amount);
        System.out.println("Order placed with Cash on Delivery.");
        System.out.println("Amount to be collected: " + formatCurrency(amount));
        return true;
    }

    @Override
    public String getPaymentType() {
        return "CASH_ON_DELIVERY";
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