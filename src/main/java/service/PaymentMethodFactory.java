package service;

import java.util.List;

import payment.CardPayment;
import payment.CashOnDelivery;
import payment.EftPayment;
import payment.PaymentMethod;
import payment.WalletPayment;

public class PaymentMethodFactory {

    public static List<PaymentMethod> getAvailableMethods() {
        //In a real application, these would be dynamically created based on user input or configuration. Dummy data is used here for demonstration purposes.
        return List.of(
            new CardPayment("4242424242424242", "Lethabo", "12/30", "123"),
            new EftPayment("CAPITEC", "1234567890", "Lethabo"),
            new WalletPayment("wallet_123", "0000"),
            new CashOnDelivery()
        );
    }
}