package service;

import java.util.List;

import domain.payment.CardPayment;
import domain.payment.CashOnDelivery;
import domain.payment.EftPayment;
import domain.payment.PaymentMethod;
import domain.payment.WalletPayment;

public class PaymentMethodFactory {

    public static List<PaymentMethod> getAvailableMethods() {
        return List.of(
            new CardPayment("4242424242424242", "Test User", "12/30", "123"),
            new EftPayment("CAPITEC", "1234567890", "Test Account"),
            new WalletPayment("wallet_123", "0000"),
            new CashOnDelivery()
        );
    }
}