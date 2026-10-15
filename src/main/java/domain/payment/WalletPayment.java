package domain.payment;

import java.math.BigDecimal;
import java.util.Locale;

public class WalletPayment implements PaymentMethod {

    private final String walletProvider; // e.g. PayPal, Ozow, Zapper, ApplePay, Google Pay, etc.
    private final String walletId;

    public WalletPayment(String walletProvider, String walletId) {
        if (walletProvider == null || walletProvider.isBlank()) {
            throw new IllegalArgumentException("Wallet provider is required");
        }
        if (walletId == null || walletId.isBlank()) {
            throw new IllegalArgumentException("Wallet ID is required");
        }
        this.walletProvider = walletProvider;
        this.walletId = walletId;
    }

    @Override
    public boolean processPayment(BigDecimal amount) {
        validateAmount(amount);
        boolean providerValid = walletProvider.length() > 0;
        
        System.out.println("Processing Wallet payment of " + formatCurrency(amount));
        System.out.println("Provider: " + walletProvider);
        System.out.println("Wallet ID: **** " + getLastFourDigits(walletId));
        System.out.println("Payment Successful!");
        
        return providerValid;
    }

    @Override
    public String getPaymentType() {
        return "WALLET";
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
    }

    private String getLastFourDigits(String id) {
        return id.length() >= 4 ? id.substring(id.length() - 4) : id;
    }

    private String formatCurrency(BigDecimal amount) {
        return "R" + String.format(Locale.US, "%,.2f", amount);
    }
}