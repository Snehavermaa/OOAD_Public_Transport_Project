# UML Diagrams for Payment and Reports Module

Here is the requested UML documentation in PlantUML format.

## 1. Class Diagram (Payment & Reports)

```plantuml
@startuml
package "MVC Controller Layer" {
  class PaymentController {
    - paymentService : PaymentService
    - bookingService : BookingService
    + showPaymentForm(bookingId: Long, model: Model) : String
    + processPayment(bookingId: Long, amount: double, method: String, detail: String, model: Model) : String
  }
  
  class ReportController {
    - reportService : ReportService
    + displayReports(model: Model) : String
  }
}

package "MVC Service Layer" {
  class PaymentService {
    - transactionRepository : TransactionRepository
    - bookingRepository : BookingRepository
    + processPayment(bookingId: Long, paymentContext: PaymentContext, amount: double) : Transaction
  }

  class ReportService {
    - transactionRepository : TransactionRepository
    - bookingRepository : BookingRepository
    + getBookingHistory() : List<Booking>
    + getTransactionHistory() : List<Transaction>
    + getTotalRevenue() : double
    + getRevenueByPaymentMethod() : Map<String, Double>
  }
}

package "Strategy Pattern (Payment)" {
  interface PaymentStrategy {
    + pay(amount: double) : boolean
    + getPaymentMethodName() : String
  }
  
  class CreditCardPayment {
    - cardNumber : String
    - cvv : String
    - expiryDate : String
    + pay(amount: double) : boolean
  }
  
  class UPIPayment {
    - upiId : String
    + pay(amount: double) : boolean
  }
  
  class WalletPayment {
    - walletId : String
    + pay(amount: double) : boolean
  }
  
  class PaymentContext {
    - paymentStrategy : PaymentStrategy
    + setPaymentStrategy(strategy: PaymentStrategy) : void
    + executePayment(amount: double) : boolean
  }
  
  PaymentStrategy <|.. CreditCardPayment
  PaymentStrategy <|.. UPIPayment
  PaymentStrategy <|.. WalletPayment
  PaymentContext o--> PaymentStrategy
}

package "MVC Model & Repository Layer (JDBC)" {
  class Transaction {
    - transactionId : Long
    - userId : Long
    - bookingId : Long
    - amount : double
    - paymentMethod : String
    - status : String
    - timestamp : Date
  }

  class TransactionRepository <<JDBC>> {
    - dataSource : DataSource
    + save(transaction: Transaction) : void
    + findAll() : List<Transaction>
    + findByUserId(userId: Long) : List<Transaction>
    + getTotalRevenue() : double
  }
}

PaymentController --> PaymentService
PaymentController --> PaymentContext
ReportController --> ReportService
PaymentService --> TransactionRepository
PaymentService --> PaymentContext
ReportService --> TransactionRepository
TransactionRepository --> Transaction
@enduml
```

## 2. Activity Diagram (Payment Flow)

```plantuml
@startuml
start
:User initiates Booking;
:System saves Booking with status **PENDING_PAYMENT**;
:System redirects User to **Payment Form** (/payment/new);
:User selects Payment Method (CreditCard / UPI / Wallet);
:User enters details and clicks **Pay Now**;
:POST /payment/process;
:PaymentController initializes **PaymentContext**;
if (Method == "UPI"?) then (yes)
  :Set Strategy = **UPIPayment**;
elseif (Method == "CreditCard"?) then (yes)
  :Set Strategy = **CreditCardPayment**;
else (Wallet)
  :Set Strategy = **WalletPayment**;
endif
:PaymentService calls **context.executePayment()**;
if (Gateway Success?) then (yes)
  :Update Booking Status to **CONFIRMED**;
  :Save Transaction with Status **SUCCESS** via JDBC;
else (no)
  :Save Transaction with Status **FAILED** via JDBC;
endif
:Redirect User to **Payment Success / Failure Page**;
stop
@enduml
```

## 3. State Diagram (Transaction States)

```plantuml
@startuml
[*] --> INIT : User accesses Payment Form

state INIT {
  [*] -> PendingPaymentSelection
  PendingPaymentSelection : User is selecting Method
}

INIT --> IN_PROGRESS : User clicks 'Pay Now'

state IN_PROGRESS {
  [*] -> ValidatingDetails
  ValidatingDetails -> ExecutingStrategy
}

IN_PROGRESS --> SUCCESS : Gateway returns true
IN_PROGRESS --> FAILED : Gateway returns false / Exception

SUCCESS --> [*]
FAILED --> [*] : Can retry (Back to INIT)
@enduml
```
