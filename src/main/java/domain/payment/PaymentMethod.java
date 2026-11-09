package domain.payment;

import java.math.BigDecimal;

public interface PaymentMethod {
    /**
     * Process a payment for the given amount
     * @param amount the amount to charge
     * @return true if payment succeeded, false otherwise
     */
    boolean processPayment(BigDecimal amount);

    /**
     * Get the type of payment for receipts/logs
     * @return CARD, EFT, WALLET, CASH_ON_DELIVERY etc
     */
    String getPaymentType();
}