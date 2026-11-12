# PayNest Demo

## Overview

PayNest is a plain Java checkout demo. It creates an order, prompts the customer to select a payment rail, processes the order total through a shared `PaymentMethod` interface, and prints a receipt after successful payment.

Currency calculations use `BigDecimal` to avoid floating-point rounding errors. The order total is the sum of each item's price multiplied by its quantity.

## Prerequisites

- Java 21
- Maven Wrapper (`mvnw` and `mvnw.cmd` are included)
- No payment gateway SDKs or external services are used; payments are simulated.

## Run the application

### Windows (PowerShell)

```powershell
.\mvnw.cmd clean package
java -jar target/merchant-order-desk-and-catalogue-engine-1.0.0-SNAPSHOT.jar
```

### macOS / Linux

```bash
./mvnw clean package
java -jar target/merchant-order-desk-and-catalogue-engine-1.0.0-SNAPSHOT.jar
```

The application builds a demo order with a total of R17,300.00 and prompts for a payment method. Enter the number for Card, EFT, or Wallet, or enter `0` to cancel. Cash on Delivery is only offered when the order total is positive and no more than R1,000.00; it is therefore excluded from the default demo order. Invalid selections are rejected. If a payment is declined, checkout returns to the menu so another method can be selected or checkout can be cancelled.

The payment credentials and outcomes are sample data supplied by `PaymentMethodFactory`; this demo does not collect real card, bank, or wallet details.

## Example

For the default order, the menu and a successful EFT checkout look like this (the customer enters `2`):

```text
Choose a payment method:
1) CARD
2) EFT
3) WALLET
0) Cancel checkout
Selection: 2
Processing EFT payment of R17,300.00
Bank: CAPITEC
Account Holder: Lethabo
Account Number: **** 7890
Payment Successful!
Payment successful via EFT
Amount: R17,300.00

Checkout complete. Printing receipt...

=================================
 PAYNEST RECEIPT
=================================
Order ID: 101
Customer: Lethabo
---------------------------------
PS5 x1 R15,600.00
Controller x2 R1,700.00
---------------------------------
TOTAL: R17,300.00
=================================
```

## Design

`PaymentMethod` defines the common payment contract, including processing an amount, identifying the rail, and reporting whether it is available for that amount. `AbstractPaymentMethod` centralizes positive-amount validation. `CardPayment`, `EftPayment`, `WalletPayment`, and `CashOnDelivery` extend it with rail-specific behavior. The demo wallet starts with a configured balance of R20,000.00, declines payments above that balance, and deducts successful payments. Cash on Delivery retains its own order-total limit. `PaymentProcessor` checks availability and processes payments through the common contract, while `Order` remains responsible for calculating the order total.

This keeps rail-specific behavior out of order arithmetic. Adding a payment rail requires extending `AbstractPaymentMethod` with its rail-specific behavior and wiring it into the composition root (`PaymentMethodFactory`), rather than changing `Order`.

## Project structure

```text
unified-payment-system/
├── src/
│   ├── main/
│   │   └── java/
│   │       ├── app/
│   │       │   └── PayNestApplication.java
│   │       ├── domain/
│   │       │   ├── Customer.java
│   │       │   ├── Order.java
│   │       │   ├── OrderItem.java
│   │       │   └── Product.java
│   │       ├── payment/
│   │       │   ├── AbstractPaymentMethod.java
│   │       │   ├── CardPayment.java
│   │       │   ├── CashOnDelivery.java
│   │       │   ├── EftPayment.java
│   │       │   ├── PaymentMethod.java
│   │       │   └── WalletPayment.java
│   │       ├── service/
│   │       │   ├── OrderService.java
│   │       │   ├── PaymentMethodFactory.java
│   │       │   ├── PaymentProcessor.java
│   │       │   └── ProductCatalog.java
│   │       └── summary/
│   │           └── ReceiptPrinter.java
│   └── test/
│       └── java/
│           ├── app/
│           │   └── PayNestApplicationTest.java
│           ├── domain/
│           │   └── OrderTest.java
│           └── payment/
│               └── WalletPaymentTest.java
├── mvnw
├── mvnw.cmd
├── pom.xml
└── README.md
```

## Tests

The JUnit tests cover order calculations, shared amount validation, wallet balance checks and debits, payment failures, interactive selection and cancellation, and Cash on Delivery's amount eligibility.

### Windows

```powershell
.\mvnw.cmd test
```

### macOS / Linux

```bash
./mvnw test
```
