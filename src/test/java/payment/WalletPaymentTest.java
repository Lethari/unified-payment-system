package payment;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class WalletPaymentTest {

    @Test
    void declinesPaymentAboveBalanceWithoutChangingBalance() {
        WalletPayment wallet = new WalletPayment(
                "PayNest", "wallet-1234", new BigDecimal("50.00"));

        assertFalse(wallet.isAvailableFor(new BigDecimal("50.01")));
        assertFalse(wallet.processPayment(new BigDecimal("50.01")));
        assertEquals(new BigDecimal("50.00"), wallet.getBalance());
    }

    @Test
    void deductsSuccessfulPaymentFromBalance() {
        WalletPayment wallet = new WalletPayment(
                "PayNest", "wallet-1234", new BigDecimal("50.00"));

        assertTrue(wallet.isAvailableFor(new BigDecimal("20.00")));
        assertTrue(wallet.processPayment(new BigDecimal("20.00")));
        assertEquals(new BigDecimal("30.00"), wallet.getBalance());
    }

    @Test
    void rejectsNonPositivePaymentAmount() {
        WalletPayment wallet = new WalletPayment(
                "PayNest", "wallet-1234", new BigDecimal("50.00"));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> wallet.processPayment(BigDecimal.ZERO));
        assertNotNull(exception);
    }
}
