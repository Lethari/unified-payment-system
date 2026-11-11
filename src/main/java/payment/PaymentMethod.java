package payment;

import java.math.BigDecimal;

public interface PaymentMethod {
    /**
     * Process a payment for the given amount
     * @param amount the amount to charge
     * @return true if payment succeeded, false otherwise
     */
    boolean processPayment(BigDecimal amount);

    /**
     * Indicates whether this payment method is available for the given amount.
     * @param amount the amount to charge
     * @return true if this method can be offered for the amount
     */
    default boolean isAvailableFor(BigDecimal amount) {
        return true;
    }

    /**
     * Get the type of payment for receipts/logs
     * @return a stable human-readable label such as Card, EFT, Wallet, or Cash on Delivery
     */
    String getPaymentType();
}