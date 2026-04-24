package com.pluralsight;

import java.util.ArrayList;
import java.util.Scanner;

public class AccountLedgerApplication {

    //My variables that I will use throughout the application.
    static Scanner userInput = new Scanner(System.in);
    static ArrayList<AccountLedger> ledgerList = new ArrayList<>();
    static AccountLedger ledger1 = new AccountLedger("", 0, "", "", "");
    static String userInputString;



    public static void main(String[] args) {

        System.out.println("Welcome to Mucci's Account Ledger Application");

        loadLedger();

        while (true) {

            mainMenu();

            System.out.print("Enter your choice: ");
            userInputString = userInput.nextLine().toUpperCase();

            switch (userInputString) {
                case ("D") -> addDeposit();
                case ("P") -> makePayment();
                case ("L") -> displayLedger();
                case ("X") -> {
                    saveLedger();
                    System.out.println("Exiting Application.\n Thank you for using Mucci's Account Ledger Application!");
                    System.exit(0);
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }

            System.out.println("Press Enter to continue.");
            userInput.nextLine();

        }

    }

    //My main menu that will be used to display the Home Screen and it's options.
    static void mainMenu() {

        System.out.println("""
                Home Screen:
                D) Add Deposit.
                P) Make Payment(Debit).
                L) View Ledger.
                X) Exit.
                """);
    }

    //My method that I will use to load the ledger from a file.
    static void loadLedger() {
        System.out.println("Loading Ledger...");

    }

    //My method that I will use to save the ledger to a file.
    static void saveLedger() {
        System.out.println("Saving all Ledger changes...");

    }

    //My method that I will use to add a deposit and a payment to the ledger.
    static void addDeposit() {
        System.out.println("Adding Deposit...");

    }

    //My method that I will use to make a payment to the ledger.
    static void makePayment() {
        System.out.println("Making Payment...");

    }

    //My method that I will use to display the ledger entries.
    static void displayLedger() {
        System.out.println("Displaying Ledger entries...");

        while (true) {

            System.out.println("""
                Ledger:
                A) All
                D) Deposits
                P) Payments
                R) Reports
                H) Home
                """);

            System.out.print("Enter your choice: ");
            userInputString = userInput.nextLine().toUpperCase();

            switch (userInputString) {
                case "A" -> displayAllEntries();
                case "D" -> displayDeposits();
                case "P" -> displayPayments();
                case "R" -> displayReports();
                case "H" -> {
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }

            System.out.println("Press Enter to continue.");
            userInput.nextLine();
        }
    }

    //My method that I will use to display the ledger entries.
    static void displayAllEntries() {

        System.out.println("Displaying all ledger entries.");

    }

    //My method that I will use to display the ledger entries that are deposits.
    static void displayDeposits() {
        System.out.println("Displaying all deposits.");

    }

    //My method that I will use to display the ledger entries that are payments (only the negative entries).
    static void displayPayments() {
        System.out.println("Displaying all payments.");

    }

    //My method that I will use to display the reports and all filter options available.
    static void displayReports() {
        System.out.println("""
                Reports:
                1) Month To Date
                2) Previous Month
                3) Year To Date
                4) Previous Year
                5) Search by Vendor
                0) Back
                """);
        while (true) {
            System.out.print("Enter Choice: ");
            userInputString = userInput.nextLine().toUpperCase();

            switch (userInputString) {
                case "1" -> monthToDateFilter();
                case "2" -> previousMonthFilter();
                case "3" -> yearToDateFilter();
                case "4" -> previousYearFilter();
                case "5" -> searchByVendorFilter();
                case "0" -> {
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }

        }
    }

    static void monthToDateFilter() {
        System.out.println("Displaying Month To Date Report.");

    }
    static void previousMonthFilter() {
        System.out.println("Displaying Previous Month Report.");
    }
    static void yearToDateFilter() {
        System.out.println("Displaying Year To Date Report.");
    }
    static void previousYearFilter() {
        System.out.println("Displaying Previous Year Report.");
    }
    static void searchByVendorFilter() {
        System.out.println("Displaying Report by Vendor.");
    }
}