package domain.payment;

import java.math.BigDecimal;
import java.util.Locale;

public class EftPayment implements PaymentMethod {

    private final String bankName;
    private final String accountNumber;
    private final String accountHolder;

    public EftPayment(String bankName, String accountNumber, String accountHolder) {
        if (bankName == null || bankName.isBlank()) {
            throw new IllegalArgumentException("Bank name is required");
        }
        if (accountNumber == null || accountNumber.length() < 8) {
            throw new IllegalArgumentException("Invalid account number");
        }
        if (accountHolder == null || accountHolder.isBlank()) {
            throw new IllegalArgumentException("Account holder name is required");
        }
        this.bankName = bankName;
        this.accountNumber = accountNumber.replaceAll("\\s", "");
        this.accountHolder = accountHolder;
    }

    @Override
    public boolean processPayment(BigDecimal amount) {
        validateAmount(amount);
        
        System.out.println("Processing EFT payment of " + formatCurrency(amount));
        System.out.println("Bank: " + bankName);
        System.out.println("Account Holder: " + accountHolder);
        System.out.println("Account Number: **** " + getLastFourDigits(accountNumber));
        System.out.println("Payment Successful!");
        
        return true;
    }

    @Override
    public String getPaymentType() {
        return "EFT";
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
    }

    private String getLastFourDigits(String acc) {
        return acc.substring(acc.length() - 4);
    }

    private String formatCurrency(BigDecimal amount) {
        return "R" + String.format(Locale.US, "%,.2f", amount);
    }
}