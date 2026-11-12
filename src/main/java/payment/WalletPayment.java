package payment;

import java.math.BigDecimal;
import java.util.Locale;

public class WalletPayment extends AbstractPaymentMethod {

    private final String walletProvider; // e.g. PayPal, Ozow, Zapper, ApplePay, Google Pay, etc.
    private final String walletId;
    private BigDecimal balance;

    public WalletPayment(String walletProvider, String walletId, BigDecimal balance) {
        if (walletProvider == null || walletProvider.isBlank()) {
            throw new IllegalArgumentException("Wallet provider is required");
        }
        if (walletId == null || walletId.isBlank()) {
            throw new IllegalArgumentException("Wallet ID is required");
        }
        if (balance == null || balance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Wallet balance cannot be null or negative");
        }
        this.walletProvider = walletProvider;
        this.walletId = walletId;
        this.balance = balance;
    }

    @Override
    public boolean processPayment(BigDecimal amount) {
        validateAmount(amount);

        if (!isAvailableFor(amount)) {
            System.out.println("Insufficient wallet balance for "
                    + formatCurrency(amount));
            return false;
        }

        balance = balance.subtract(amount);
        System.out.println("Processing Wallet payment of " + formatCurrency(amount));
        System.out.println("Provider: " + walletProvider);
        System.out.println("Wallet ID: **** " + getLastFourDigits(walletId));
        System.out.println("Payment Successful!");

        return true;
    }

    @Override
    public boolean isAvailableFor(BigDecimal amount) {
        return amount != null
                && amount.compareTo(BigDecimal.ZERO) > 0
                && amount.compareTo(balance) <= 0;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    @Override
    public String getPaymentType() {
        return "WALLET";
    }

    private String getLastFourDigits(String id) {
        return id.length() >= 4 ? id.substring(id.length() - 4) : id;
    }

    private String formatCurrency(BigDecimal amount) {
        return "R" + String.format(Locale.US, "%,.2f", amount);
    }
}