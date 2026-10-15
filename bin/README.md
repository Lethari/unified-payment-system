# PayNest Demo

## Overview
This demo models a simple customer order flow and prints a receipt after a successful payment. It also demonstrates how interface-based payment rails let the checkout funnel work with any supported payment method without baking payment-specific logic into the checkout or order model.

## Prerequisites
- Java 21
- Maven Wrapper included
- JUit 5 for tests
- Plain Java used

## How to run
On Windows:
```powershell
git clone https://github.com/Umuzi-skills1ab/unified-checkout-across-payment-rails-Lethari.git
cd unified-checkout-across-payment-rails-Lethari
.\mvnw.cmd clean compile exec:java -Dexec.mainClass="app.PayNestApplication"
```
On macOS/Linux:
```bash
git clone https://github.com/Umuzi-skills1ab/unified-checkout-across-payment-rails-Lethari.git
cd unified-checkout-across-payment-rails-Lethari
./mvnw.cmd clean compile exec:java -Dexec.mainClass="app.PayNestApplication"
```
After packaging, run the generated JAR:
Powershell code
.\mvnw.cmd clean package
java -jar target/merchant-order-desk-and-catalogue-engine-1.0.0-SNAPSHOT.jar

## What this demonstrates
1. Customer and Order domain objects are created.
2. OrderService adds products to the order.
3. `PaymentProcessor` sends the calculated order total to a `PaymentMethod` implementation.
4. `PaymentMethod` implementations: CardPayment, EftPayment, WalletPayment,CashOnDelivery.
5. ReceiptPrinter prints the order receipt only after payment succeeds.
6. `PaymentMethodFactory` provides an unmodifiable list of paymentmethods, so main does not construct payment objects directly.

## Why interfaces are better than a single mega-method here
Using `PaymentMethod` keeps checkout logic independent of concrete payment rails. That means adding a new rail only requires a new implementation class and wiring at the application level, instead of editing a large switch or if/else block in core checkout logic. It also keeps the order model focused on arithmetic and ensures the charged amount comes from a single order total calculation.

## Optional class diagram
```
PaymentMethod
  |-- CardPayment
  |-- EftPayment
  |-- WalletPayment
  |-- CashOnDelivery
```

## Expected output
After running, the terminal should show a payment success message and a formatted receipt similar to:

```text
Payment successful via CARD
Amount: R17,300.00

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
NOTE: To test other payment methods, change
`PaymentMethodFactory.getAvailableMethods().get(0)`
in `PayNestApplication.java`
to `.get(0)` for CARD, `.get(1)` for EFT, `.get(2)` for WALLET, `.get(3)` for COD.

