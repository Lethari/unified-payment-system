package service;

import java.math.BigDecimal;
import java.util.Locale;

import domain.payment.PaymentMethod;

public class PaymentProcessor {
    public boolean processPayment(PaymentMethod method, BigDecimal amount) {
        boolean success = method.processPayment(amount);

        if (success) {
            System.out.println("Payment successful via " + method.getPaymentType());
            System.out.println("Amount: " + formatCurrency(amount));
        } else {
            System.out.println("Payment failed via " + method.getPaymentType());
        }

        return success;
    }

    private String formatCurrency(BigDecimal amount) {
        return "R" + String.format(Locale.US, "%,.2f", amount);
    }
}
