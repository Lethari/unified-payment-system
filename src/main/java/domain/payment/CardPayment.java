package domain.payment;

import java.math.BigDecimal;
import java.util.Locale;

public class CardPayment implements PaymentMethod {

    private final String cardNumber;
    private final String cardHolder;
    private final String expiryDate;
    private final String cvv;

    public CardPayment(String cardNumber, String cardHolder, String expiryDate, String cvv) {
        String cleanCard = cardNumber.replaceAll("\\s", "");
        
        // Validate card number
        if (cleanCard == null || cleanCard.isBlank() || cleanCard.length() < 13 || cleanCard.length() > 19) {
            throw new IllegalArgumentException("Invalid card number");
        }
        
        // Validate card holder
        if (cardHolder == null || cardHolder.isBlank()) {
            throw new IllegalArgumentException("Card holder name is required");
        }
        
        // Validate expiry date format MM/YY
        if (expiryDate == null || !expiryDate.matches("(0[1-9]|1[0-2])/\\d{2}")) {
            throw new IllegalArgumentException("Expiry date must be in MM/YY format");
        }
        
        // Validate CVV - 3 or 4 digits for Amex
        if (cvv == null || !cvv.matches("\\d{3,4}")) {
            throw new IllegalArgumentException("CVV must be 3 or 4 digits");
        }

        this.cardNumber = cleanCard;
        this.cardHolder = cardHolder;
        this.expiryDate = expiryDate;
        this.cvv = cvv;
    }

    @Override
    public boolean processPayment(BigDecimal amount) {
        validateAmount(amount);
        
        boolean validForGateway = expiryDate.length() > 0 && cvv.length() > 0;
        
        System.out.println("Processing Card payment of " + formatCurrency(amount));
        System.out.println("Card Holder: " + cardHolder);
        System.out.println("Card Number: **** " + getLastFourDigits());
        System.out.println("Payment Successful!");
        
        return validForGateway;
    }

    @Override
    public String getPaymentType() {
        return "CARD";
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
    }

    private String getLastFourDigits() {
        return cardNumber.substring(cardNumber.length() - 4);
    }

    private String formatCurrency(BigDecimal amount) {
        return "R" + String.format(Locale.US, "%,.2f", amount);
    }
}