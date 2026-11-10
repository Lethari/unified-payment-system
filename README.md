# PayNest Demo

## Overview
This demo models a simple customer order flow and prints a receipt after a successful payment. It also demonstrates how interface-based payment rails let the checkout funnel work with any supported payment method without baking payment-specific logic into the checkout or order model.

## Prerequisites
- Java 21
- Maven Wrapper included (`mvnw` and `mvnw.cmd`)
- JUnit 5 for tests
- Plain Java (No Spring Boot)

## How to Run

### Windows (PowerShell)

```powershell
git clone https://github.com/Lethari/unified-payment-system.git
cd unified-payment-system

.\mvnw.cmd clean package
java -jar target/merchant-order-desk-and-catalogue-engine-1.0.0-SNAPSHOT.jar
```

### macOS / Linux

```bash
git clone https://github.com/Lethari/unified-payment-system.git
cd unified-payment-system

./mvnw clean package
java -jar target/merchant-order-desk-and-catalogue-engine-1.0.0-SNAPSHOT.jar
```

## What this demonstrates
1. Customer and Order domain objects are created.
2. OrderService adds products to the order.
3. `PaymentProcessor` sends the calculated order total to a `PaymentMethod` implementation.
4. `PaymentMethod` implementations: CardPayment, EftPayment, WalletPayment, CashOnDelivery.
5. ReceiptPrinter prints the order receipt only after payment succeeds.
6. `PaymentMethodFactory` provides an unmodifiable list of payment methods, allowing the application to select a payment method without directly constructing payment objects.


## Why Interfaces Are Better Than a Single Mega-Method
Using the `PaymentMethod` interface keeps checkout logic independent of specific payment implementations.
This allows new payment methods to be introduced without modifying the core checkout logic or adding large `if/else` or `switch` statements.
Each payment implementation handles its own payment-specific behaviour, while `PaymentProcessor` remains responsible for processing payments.
This separation improves maintainability, flexibility, and extensibility.

## Project Structure

```text
unified-payment-system/
├── src/
│ ├── main/
│ │ └── java/
│ │ ├── app/
│ │ │ └── PayNestApplication.java
│ │ ├── domain/
│ │ │ ├── Customer.java
│ │ │ ├── Order.java
│ │ │ ├── OrderItem.java
│ │ │ └── Product.java
│ │ ├── payment/
│ │ │ ├── CardPayment.java
│ │ │ ├── CashOnDelivery.java
│ │ │ ├── EftPayment.java
│ │ │ ├── PaymentMethod.java
│ │ │ └── WalletPayment.java
│ │ ├── service/
│ │ │ ├── OrderService.java
│ │ │ ├── PaymentMethodFactory.java
│ │ │ ├── PaymentProcessor.java
│ │ │ └── ProductCatalog.java
│ │ └── summary/
│ │ └── ReceiptPrinter.java
│ └── test/
│ └── java/
│ └── domain/
│ └── OrderTest.java
├── mvnw
├── mvnw.cmd
├── pom.xml
└── README.md
```

## Optional Class Diagram

```text
             PaymentMethod
                   |
       +-----------+-----------+-----------+
       | | | |
  CardPayment EftPayment WalletPayment CashOnDelivery
```

## Expected output
After running the application, the terminal displays a successful payment message follwed by a formatted receipt.

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

### Testing Different Payment Methods
To test other payment methods, change the selected index in `PayNestApplication.java` by `PaymentMethodFactory` to:

- `.get(0)`   - Card
- `.get(1)`   - EFT
- `.get(2)`   - Wallet
- `.get(3)`   - Cash on Delivery


## Testing
The project includes tests covering order functionality, payment validation, and related behavior.

Run the following command to execute the JUnit test suite:

### Windows

```powershell
.\mvnw.cmd test
```

### macOS / Linux

``` bash
./mvwn test
```


