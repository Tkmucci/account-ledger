# Account Ledger Application

A command-line Java application for tracking personal financial transactions. Users can record deposits and payments, view their ledger, and generate filtered reports.

## Features

- **Add Deposit** — Record an income/deposit transaction with description, vendor, and amount
  - Validates that description and vendor are non-empty and amount is positive
  - Displays a confirmation prompt before saving
  - Automatically timestamps using Central Time (America/Chicago)
- **Make Payment** — Record a debit/payment transaction (stored as a negative amount)
  - Same input validation and confirmation as deposits
- **View Ledger** — Browse all entries, deposits only, or payments only
  - All views are sorted newest-first by date and time
- **Reports** — Filter transactions by:
  - Month to Date — entries from the 1st of the current month through today
  - Previous Month — all entries from last month (current year only)
  - Year to Date — all entries from January 1st through today
  - Previous Year — all entries from the prior calendar year
  - Search by Vendor — case-insensitive partial vendor name match (matches any entry containing the search term)
  - Month to Month — custom month range within the current year
- **Persistent Storage** — Transactions are loaded from and saved to `transactions.csv` on startup/exit
  - File is written sorted newest-first on save

## Related Project

Looking for a version with a graphical interface? Check out the **[Account Ledger — GUI Edition](https://github.com/Tkmucci/account-ledger-personal-with-a-gui)**, which builds on this application with a full GUI, user account creation, and login functionality.

## Prerequisites

- Java 17+
- Maven
- Timezone: application timestamps use **America/Chicago (Central Time)**

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
│       │   ├── AccountLedgerApplication.java   # Main application logic, menus, and report filters
│       │   └── AccountLedger.java              # Transaction data model (date, time, description, vendor, amount)
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
| 6   | Month to Month (current year only) |
| 0   | Back |
