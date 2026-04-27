# Account Ledger Application

A command-line Java application for tracking personal financial transactions. Users can record deposits and payments, view their ledger, and generate filtered reports.

## Features

- **Add Deposit** — Record an income/deposit transaction with description, vendor, and amount
- **Make Payment** — Record a debit/payment transaction (stored as a negative amount)
- **View Ledger** — Browse all entries, deposits only, or payments only
- **Reports** — Filter transactions by:
  - Month to Date
  - Previous Month
  - Year to Date
  - Previous Year
  - Search by Vendor
  - Month to Month (custom range)
- **Persistent Storage** — Transactions are loaded from and saved to `transactions.csv` on startup/exit

## Prerequisites

- Java 17+
- Maven

## Running the Application

```bash
mvn compile exec:java -Dexec.mainClass="com.pluralsight.AccountLedgerApplication"
```

Or build and run the JAR:

```bash
mvn package
java -cp target/account-ledger-1.0-SNAPSHOT.jar com.pluralsight.AccountLedgerApplication
```

## Data Format

Transactions are stored in `src/main/resources/transactions.csv` using pipe-delimited format:

```
Date|Time|Description|Vendor|Amount
2024-01-15|10:30:00|Paycheck|Employer Inc|2500.00
2024-01-16|14:00:00|Groceries|Whole Foods|-85.50
```

- Deposits have a **positive** amount
- Payments/debits have a **negative** amount

## Project Structure

```
account-ledger/
├── src/
│   └── main/
│       ├── java/com/pluralsight/
│       │   ├── AccountLedgerApplication.java   # Main application logic & menus
│       │   └── AccountLedger.java              # Transaction data model
│       └── resources/
│           └── transactions.csv                # Persistent transaction data
└── pom.xml
```

## Navigation

### Home Screen
| Key | Action |
|-----|--------|
| D   | Add Deposit |
| P   | Make Payment |
| L   | View Ledger |
| X   | Exit |

### Ledger Menu
| Key | Action |
|-----|--------|
| A   | All entries |
| D   | Deposits only |
| P   | Payments only |
| R   | Reports |
| H   | Back to Home |

### Reports Menu
| Key | Action |
|-----|--------|
| 1   | Month to Date |
| 2   | Previous Month |
| 3   | Year to Date |
| 4   | Previous Year |
| 5   | Search by Vendor |
| 6   | Month to Month |
| 0   | Back |
