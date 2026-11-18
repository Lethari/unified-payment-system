package service;

import java.math.BigDecimal;

import money.RandFormatter;
import payment.PaymentMethod;

public class PaymentProcessor {
    public boolean processPayment(PaymentMethod method, BigDecimal amount) {
        
        if (method == null) {
        throw new IllegalArgumentException("Payment method cannot be null");
    }
    
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
        throw new IllegalArgumentException("Payment amount must be greater than zero");
    }

        if (!method.isAvailableFor(amount)) {
            System.out.println("Payment failed via " + method.getPaymentType());
            return false;
        }

        boolean success = method.processPayment(amount);

        if (success) {
            System.out.println(method.getSuccessMessage());
            System.out.println("Amount: " + RandFormatter.format(amount));
        } else {
            System.out.println("Payment failed via " + method.getPaymentType());
        }

        return success;
    }
}
