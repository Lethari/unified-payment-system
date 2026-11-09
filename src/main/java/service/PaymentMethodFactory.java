package service;

import java.util.List;

import domain.payment.CardPayment;
import domain.payment.CashOnDelivery;
import domain.payment.EftPayment;
import domain.payment.PaymentMethod;
import domain.payment.WalletPayment;

public class PaymentMethodFactory {

    public static List<PaymentMethod> getAvailableMethods() {
        return List.of(//In a real application, these would be dynamically created based on user input or configuration. Dummy data is used here for demonstration purposes.
            new CardPayment("4242424242424242", "Test User", "12/30", "123"),
            new EftPayment("CAPITEC", "1234567890", "Test Account"),
            new WalletPayment("wallet_123", "0000"),
            new CashOnDelivery()
        );
    }
}