package payment;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class CardPayment extends AbstractPaymentMethod {

    private final String cardNumber;
    private final String cardHolder;
    private final String expiryDate;

    public CardPayment(String cardNumber, String cardHolder, String expiryDate, String cvv) {

        // Validate card number
        if (cardNumber == null || cardNumber.isBlank()) {
            throw new IllegalArgumentException("Card number is required");
        }

        String cleanCard = cardNumber.replaceAll("\\s", "");

        if (!cleanCard.matches("\\d{13,19}")) {
            throw new IllegalArgumentException("Invalid card number");
        }

        // Validate card holder
        if (cardHolder == null || cardHolder.isBlank()) {
            throw new IllegalArgumentException("Card holder name is required");
        }

        // Validate expiry date format
        if (expiryDate == null ||
                !expiryDate.matches("(0[1-9]|1[0-2])/\\d{2}")) {
            throw new IllegalArgumentException("Expiry date must be in MM/YY format");
        }

        // Validate that the card is not expired
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yy");
        YearMonth expiry = YearMonth.parse(expiryDate, formatter);

        if (expiry.isBefore(YearMonth.now())) {
            throw new IllegalArgumentException("Card has expired");
        }

        // Validate CVV 3 for Visa/MasterCard, 4 for Amex
        if (cvv == null || !cvv.matches("\\d{3,4}")) {
            throw new IllegalArgumentException(
                    "CVV must be 3 or 4 digits");
        }

        this.cardNumber = cleanCard;
        this.cardHolder = cardHolder;
        this.expiryDate = expiryDate;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    @Override
    public boolean processPayment(BigDecimal amount) {
        validateAmount(amount);

        System.out.println("Processing Card payment of "
                + formatCurrency(amount));
        System.out.println("Card Holder: " + cardHolder);
        System.out.println("Card Number: **** " + getLastFourDigits());
        System.out.println("Payment Successful!");

        return true; // Simulated payment
    }

    @Override
    public String getPaymentType() {
        return "CARD";
    }

    private String getLastFourDigits() {
        return cardNumber.substring(cardNumber.length() - 4);
    }

    private String formatCurrency(BigDecimal amount) {
        return "R" + String.format(Locale.US, "%,.2f", amount);
    }
}
