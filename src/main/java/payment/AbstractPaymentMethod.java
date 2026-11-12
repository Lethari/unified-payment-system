package payment;

import java.math.BigDecimal;


//Abstract base class for payment methods, providing common validation logic.
public abstract class AbstractPaymentMethod implements PaymentMethod {

    protected final void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
    }
}
