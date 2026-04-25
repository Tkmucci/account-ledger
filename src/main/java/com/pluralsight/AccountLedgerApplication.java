package com.pluralsight;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class AccountLedgerApplication {

    //My variables that I will use throughout the application.
    static Scanner userInput = new Scanner(System.in);
    static ArrayList<AccountLedger> ledgerList = new ArrayList<>();
    static HashMap<Integer, String> monthsAndTheirCorrespondingNumbers = new HashMap<>();
    static String userInputString;
    static String readFileName;
    static int counter = 0;



    public static void main(String[] args) {

        //A nice welcome message to the user.
        System.out.println("Welcome to Mucci's Account Ledger Application");

        //Calling this method to load the ledger from a file.
        loadLedger();

        //While loop that will keep the application running until the user exits.
        while (true) {

            //Calling this method to display the main menu.
            mainMenu();


            System.out.print("Enter your choice: ");
            userInputString = userInput.nextLine().toUpperCase();

            //Switch statement that will handle the user's choice.'
            switch (userInputString) {

                case "D" -> addDeposit();
                case "P" -> makePayment();
                case "L" -> displayLedger();
                case "X" -> {

                    //Calling this method to save the ledger to a file
                    // before exiting the application.
                    saveLedgerUpdates();
                    System.out.println("Exiting Application.\n Thank you for using Mucci's Account Ledger Application!");
                    System.exit(0);
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }

            //Pausing the application for a moment before displaying the main menu again.
            //notifying the user that they are about to return to the main menu.
            System.out.println("Press Enter to continue.");
            userInput.nextLine();

        }

    }

    //main menu that will be used to display the Home Screen and it's options.
    static void mainMenu() {

        System.out.println("""
                Home Screen:
                D) Add Deposit.
                P) Make Payment(Debit).
                L) View Ledger.
                X) Exit.
                """);
    }

    //The method that I will use to load the ledger from a file.
    static void loadLedger() {

        System.out.println("Loading Ledger...");
        readFileName = "transactions.csv";

        //try-catch block that will handle any exceptions that may occur while
        // reading the file and loading the ledger.
        try {

            String line;
            int lineNumber = 0;

            //Creating a BufferedReader object to read the file line by line.
            BufferedReader bufReader = new BufferedReader(
                    new FileReader("src/main/resources/" + readFileName));

            //Reading the file line by line and adding each line to the ledgerList.
            while ((line = bufReader.readLine()) != null) {

                //I probably need an if-else statement here to check if the data is
                // separated by a pipe or not and then give an error message

                //Splitting the line into parts using the pipe character.
                String[] dataParts = line.split("\\|");

                //Counting the line number to skip the header line.
                lineNumber++;

                //skipping the header line.
                if (lineNumber == 1) {
                    continue;
                }

                //Adding each line to the ledgerList after validating the data format
                // and ensuring that the data parts array has the correct number of elements.
                if (dataParts.length == 5){
                    String date = dataParts[0];
                    String time = dataParts[1];
                    String description = dataParts[2];
                    String vendor = dataParts[3];
                    double amount = Double.parseDouble(dataParts[4]);
                    ledgerList.add(new AccountLedger(date,time,description,vendor,amount));

                }
                else {
                    System.out.println("Unformatted data in file cannot be loaded.");
                    //I will come back here to make sure that the user sees the data and shows
                    // them how they are supposed to format it.


                }

            }
        }
        //Displaying an error message to the user if the file is not found.
        catch (FileNotFoundException e){

            System.out.println("File not found. Please check the file name and try again.");
        }
        //Displaying an error message if there is an error reading the file
        catch (IOException e){

            System.out.println("Error reading file. Please check the file and try again.");
        }
        //Displaying an error message if the data format is incorrect.
        catch (NumberFormatException e){
            System.out.println("Invalid amount format in file. Please check the file and try again.");
        }
        //Displaying an error message if there is an unexpected error.
        catch (Exception e){
            System.out.println("An unexpected error occurred.");
            throw new RuntimeException(e);
        }

    }

    //the method that I will use to save the ledger to a file.
    static void saveLedgerUpdates() {

        System.out.println("Saving all Ledger changes...");
        readFileName = "transactions.csv";

        //try-catch block that will handle any exceptions that may occur while saving updates to the ledger.
        try {

            //BufferedWriter object to write the ledger-updated data to a file.
            BufferedWriter bufWriter = new BufferedWriter(
                    new FileWriter("src/main/resources/" + readFileName));

            //Writing the updated ledger data to the file.
            for (AccountLedger ledgerEntry : ledgerList) {
                bufWriter.write(String.format("%s|%s|%s|%s|%.2f\n",
                        ledgerEntry.getDate(),
                        ledgerEntry.getTimestamp(),
                        ledgerEntry.getDescription(),
                        ledgerEntry.getVendor(),
                        ledgerEntry.getAmount()));
            }
            bufWriter.close();
            System.out.println("Ledger updates saved successfully.");
            
        }
        //Displaying an error message if there is an error saving the ledger.
        catch (IOException e) {
            System.out.println("Error saving ledger. Please check the file and try again.");
        }
        //Displaying an error message if there is an unexpected error.
         catch (Exception e) {
            System.out.println("An unexpected error occurred.");
            throw new RuntimeException(e);
        }

    }

    //The method that I will use to add a deposit and a payment to the ledger.
    static void addDeposit() {
        System.out.println("Adding Deposit...");


    }

    //The method that I will use to make a payment to the ledger.
    static void makePayment() {
        System.out.println("Making Payment...");

    }

    //The method that I will use to display the ledger entries.
    static void displayLedger() {

        //While loop that will keep the user from exiting the ledger until they choose to exit.
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

            //Switch statement that will handle the user's choice.
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

            //Pausing the application for a moment before displaying the main menu again.
            System.out.println("Press Enter to continue.");
            userInput.nextLine();
        }
    }

    //the method that I will use to display the ledger entries.
    static void displayAllEntries() {

        //counting the number of entries in the ledger.
        for (AccountLedger ledgerEntry : ledgerList) {
            if(ledgerEntry.getAmount() != 0) {
                counter++;
            }
        }

        System.out.println("\nDisplaying all ledger " + counter + " entries.\n");

        //Checking if the ledger is empty.
        if (ledgerList.isEmpty()) {
            System.out.println("No entries in the ledger.");
            return;
        }

        //Displaying the ledger entries.
        for (AccountLedger ledgerEntry : ledgerList) {

            System.out.printf("""
                        %s | %s | %s | %s | %.2f
                        """
                    , ledgerEntry.getDate()
                    , ledgerEntry.getTimestamp()
                    , ledgerEntry.getDescription()
                    , ledgerEntry.getVendor()
                    , ledgerEntry.getAmount());
        }

    }

    //The method that I will use to display the ledger entries that are deposits.
    static void displayDeposits() {

        //counting the number of deposits in the ledger.
        for (AccountLedger ledgerEntry : ledgerList) {
            if(ledgerEntry.getAmount() > 0) {
                counter++;
            }
        }

        System.out.println("\nDisplaying all " + counter + " deposits.\n");

        //Checking if the ledger is empty.
        if (counter == 0) {
            System.out.println("There are no deposits to display.");
            return;
        }

        //Displaying the Deposits.
        for (AccountLedger ledgerEntry : ledgerList) {

            if (ledgerEntry.getAmount() >= 0) {
                System.out.printf("""
                                %s | %s | %s | %s | %.2f
                                """
                        , ledgerEntry.getDate()
                        , ledgerEntry.getTimestamp()
                        , ledgerEntry.getDescription()
                        , ledgerEntry.getVendor()
                        , ledgerEntry.getAmount());
            }
        }


    }

    //the method that I will use to display the ledger entries that are payments (only the negative entries).
    static void displayPayments() {

        //counting the number of payments in the ledger.
        for (AccountLedger ledgerEntry : ledgerList) {
            if(ledgerEntry.getAmount() < 0) {
                counter++;
            }
        }
        System.out.println("\nDisplaying all " + counter + " payments.\n");

        //Checking if the ledger is empty.
        if (counter == 0) {
            System.out.println("There are no payments to display.");
            return;
        }

        //Displaying the payments.
        for (AccountLedger ledgerEntry : ledgerList) {

            if (ledgerEntry.getAmount() < 0) {
                System.out.printf("""
                                %s | %s | %s | %s | %.2f
                                """
                        , ledgerEntry.getDate()
                        , ledgerEntry.getTimestamp()
                        , ledgerEntry.getDescription()
                        , ledgerEntry.getVendor()
                        , ledgerEntry.getAmount());
            }
        }

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

        //While loop that will keep the user from exiting the reports until they choose to exit.
        while (true) {

            System.out.print("Enter Choice: ");
            userInputString = userInput.nextLine().toUpperCase();

            //Switch statement that will handle the user's choice.
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
        System.out.println("Displaying Month To Date Report features coming soon.");

    }
    static void previousMonthFilter() {
        System.out.println("Displaying Previous Month Report features coming soon.");
    }
    static void yearToDateFilter() {
        System.out.println("Displaying Year To Date Report features coming soon.");
    }
    static void previousYearFilter() {
        System.out.println("Displaying Previous Year Report features coming soon.");
    }
    static void searchByVendorFilter() {
        System.out.println("Displaying Report by Vendor features coming soon.");
    }
}