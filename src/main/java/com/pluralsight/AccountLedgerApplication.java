package com.pluralsight;

import java.io.*;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class AccountLedgerApplication {

    //My variables that I will use throughout the application.
    static Scanner userInput = new Scanner(System.in);
    static ArrayList<AccountLedger> ledgerList = new ArrayList<>();
    static HashMap<Integer, String> monthsAndTheirCorrespondingNumbers = new HashMap<>();
    static String userInputString;
    static String readFileName;
    static int counter = 0;
    static ZonedDateTime currentDateTime = ZonedDateTime.now(ZoneId.of("America/Chicago"));
    static String currentDate;
    static String currentTime;
    static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    static DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss");
    static DateTimeFormatter monthFormat = DateTimeFormatter.ofPattern("yyyy-MM-01");
    static DateTimeFormatter yearFormat = DateTimeFormatter.ofPattern("yyyy-01-01");


    public static void main(String[] args) {

        //A nice welcome message to the user.
        System.out.println("Welcome to Mucci's Account Ledger Application");

        //Calling this method to load the ledger from a file.
        loadLedger();

        //Calling this method to display the main menu.
        mainMenu();

    }

    //main menu that will be used to display the Home Screen and it's options.
    static void mainMenu() {

        //While loop that will keep the application running until the user exits.
        while (true) {

            System.out.println("""
                    Home Screen:
                    D) Add Deposit.
                    P) Make Payment(Debit).
                    L) View Ledger.
                    X) Exit.
                    """);

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
                    System.out.println("Exiting Application.\nThank you for using Mucci's Account Ledger Application!");
                    System.exit(0);
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }

            //Pausing the application for a moment before displaying the main menu again.
            //notifying the user that they are about to return to the main menu.
            System.out.println("\nReturning to the Home Screen...");
            System.out.println("Press Enter to continue.");
            userInput.nextLine();

        }

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
                if (dataParts.length == 5) {
                    String date = dataParts[0];
                    String time = dataParts[1];
                    String description = dataParts[2];
                    String vendor = dataParts[3];
                    double amount = Double.parseDouble(dataParts[4]);
                    ledgerList.add(new AccountLedger(date, time, description, vendor, amount));

                } else {

                    System.out.println("Unformatted data on line " + lineNumber + " data cannot be loaded.");
                    //I will come back here to make sure that the user sees the data and shows
                    // them how they are supposed to format it.


                }

            }
        }
        //Displaying an error message to the user if the file is not found.
        catch (FileNotFoundException e) {

            System.out.println("File not found. Please check the file name and try again.");
        }
        //Displaying an error message if there is an error reading the file
        catch (IOException e) {

            System.out.println("Error reading file. Please check the file and try again.");
        }
        //Displaying an error message if the data format is incorrect.
        catch (NumberFormatException e) {
            System.out.println("Invalid amount format in file. Please check the file and try again.");
        }
        //Displaying an error message if there is an unexpected error.
        catch (Exception e) {
            System.out.println("An unexpected error occurred.");
            throw new RuntimeException(e);
        }

    }

    //the method that I will use to save the ledger to a file.
    static void saveLedgerUpdates() {

        System.out.println("Saving all Ledger changes...");
        readFileName = "transactions.csv";

        //try-catch block that will handle any exceptions that may occur while
        // saving updates to the ledger.
        try {

            //BufferedWriter object to write the ledger-updated data to a file.
            BufferedWriter bufWriter = getBufferedWriter();
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

    //getBufferedWriter method that will return a BufferedWriter object
    // that is used to write the ledger-updated data to a file.
    private static BufferedWriter getBufferedWriter() throws IOException {
        BufferedWriter bufWriter = new BufferedWriter(
                new FileWriter("src/main/resources/" + readFileName));
        bufWriter.write("Date|Time|Description|Vendor|Amount\n");

        //Writing the updated ledger data to the file.
        //Sort the ledger entries starting with the newest first before writing to the file
        // by date and then by timestamp.
        ledgerList.sort(Comparator.comparing(AccountLedger::getDate)
                .thenComparing(AccountLedger::getTimestamp)
                .reversed());
        for (AccountLedger ledgerEntry : ledgerList) {
            bufWriter.write(String.format("%s|%s|%s|%s|%.2f\n",
                    ledgerEntry.getDate(),
                    ledgerEntry.getTimestamp(),
                    ledgerEntry.getDescription(),
                    ledgerEntry.getVendor(),
                    ledgerEntry.getAmount()));
        }
        return bufWriter;
    }

    //The method that I will use to add a deposit and a payment to the ledger.
    static void addDeposit() {

        System.out.println("\nAdding a Deposit to the Ledger.");

        //My big while loop that will keep the user from exiting the application until they choose to.
        while (true) {

            //Getting the user's input for the deposit details.
            //Using a do-while loop to validate the input.
            String description;
            String vendor;
            double amount;

            //Asking the user to enter the details of the deposit and then adding it to the ledgerList.

            do {


                System.out.print("\nEnter Description: ");
                description = userInput.nextLine();

                if (description.isEmpty()) {

                    System.out.println("\n⚠️ Description cannot be empty.");
                    System.out.println("Try again.\n");
                }
                else if (description.contains("|")) {
                    System.out.println("\n⚠️ Cannot contain the '|' character.");
                    description = "";
                }

            } while (description.isEmpty() || description.contains( "|" ) );

            do {

                System.out.print("Enter Vendor: ");
                vendor = userInput.nextLine();

                if (vendor.isEmpty()) {

                    System.out.println("\n⚠️ Vendor name cannot be empty.");
                    System.out.println("Try again.\n");
                }
                else if (vendor.contains("|")) {
                    System.out.println("\n⚠️ Cannot contain the '|' character.");
                    vendor = "";
                }

            } while (vendor.isEmpty() || vendor.contains( "|" ) );


            do {

                try {

                    System.out.print("Enter Amount: $");
                    amount = userInput.nextDouble();
                    userInput.nextLine();

                    if (amount <= 0) {

                        System.out.println("\n⚠️ Amount cannot be below zero.");
                        System.out.println("Try again.\n");

                    }
                } catch (Exception e) {
                    System.out.println("\n⚠️ Invalid input. Please enter a valid number for the amount.");
                    System.out.println("Try again.\n");

                    // Clear the invalid input
                    userInput.nextLine();

                    // Set amount to an invalid value to continue the loop
                    amount = -1;
                }
            } while (amount <= 0);

            //Calling the nowTimeMethod method to get the current date and time.
            //this fixes it so that the date and time are always displayed in the correct format.
            nowDateTimeMethod();

            //displaying the deposit details to the user and asking for confirmation
            // before adding it to the ledgerList.
            System.out.printf("\nYou are depositing $%.2f from %s", amount, vendor);
            System.out.print("\nConfirm? (Y/N): ");
            String userDepositInput = userInput.nextLine();

            //checking if the user wants to add the deposit to the ledgerList with a simple if-else statement
            // and then adding it to the ledgerList if they confirm or cancelling the transaction if they do not confirm.
            if (userDepositInput.equalsIgnoreCase("Y") || userDepositInput.equalsIgnoreCase("YES")) {

                System.out.println("\nProcessing deposit information...");
                ledgerList.add(new AccountLedger(currentDate, currentTime, description, vendor, amount));
                System.out.println("Deposit added successfully.");

            } else if (userDepositInput.equalsIgnoreCase("N") || userDepositInput.equalsIgnoreCase("NO")) {

                System.out.println("\nDeposit transaction cancelled.");

            } else {

                System.out.println("\nInvalid input. Please enter Y for Yes or N for No.");

                System.out.println("\nTransaction cancelled");

                System.out.println("\nTry entering the information again.\n");
                continue;
            }


            //Giving the user the option to make another deposit or exit the application.
            System.out.print("\nWould you like to make another deposit? (Y/N): ");
            String userInputString = userInput.nextLine();


            //if the user chooses to make another deposit, the loop will continue.
            if (userInputString.equalsIgnoreCase("Yes") || userInputString.equalsIgnoreCase("Y")) {

                continue;
            }
            if (userInputString.equalsIgnoreCase("N") || userInputString.equalsIgnoreCase("NO")) {
                return;
            } else {

                System.out.println("\nInvalid input. Please enter Y for Yes or N for No.");

                System.out.println("\nTry entering the information again.\n");

            }
        }

    }

    //The method that I will use to make a payment to the ledger.
    static void makePayment() {

        System.out.println("\nMaking a Payment to the Ledger.");

        //My big while loop that will keep the user from exiting the application until they choose to.
        while (true) {

            //Getting the user's input for the payment details.
            //Using a do-while loop to validate the input.
            String description;
            String vendor;
            double amount;

            //Asking the user to enter the details of the payment and then adding it to the ledgerList.

            do {

                System.out.print("\nEnter Description: ");
                description = userInput.nextLine();

                if (description.isEmpty()) {

                    System.out.println("\n⚠️ Description cannot be empty.");
                    System.out.println("Try again.\n");
                }
                else if (description.contains("|")) {
                    System.out.println("\n⚠️ Cannot contain the '|' character.");
                    description = "";
                }

            } while (description.isEmpty() || description.contains( "|" ) );

            do {

                System.out.print("Enter Vendor: ");
                vendor = userInput.nextLine();

                if (vendor.isEmpty()) {

                    System.out.println("\n⚠️ Vendor name cannot be empty.");
                    System.out.println("Try again.\n");
                }
                else if (vendor.contains("|")) {
                    System.out.println("\n⚠️ Cannot contain the '|' character.");
                    vendor = "";
                }

            } while (vendor.isEmpty() || vendor.contains( "|" ) );

            do {

                try {
                    System.out.print("Enter Amount: $");
                    amount = userInput.nextDouble();
                    userInput.nextLine();

                    if (amount <= 0) {

                        System.out.println("\n⚠️ Amount cannot be below zero.");
                        System.out.println("Try again.\n");

                    }
                } catch (Exception e) {
                    System.out.println("\n⚠️ Invalid input. Please enter a valid number for the amount.");
                    System.out.println("Try again.\n");

                    // Clear the invalid input
                    userInput.nextLine();

                    // Set amount to an invalid value to continue the loop
                    amount = -1;
                }
            } while (amount <= 0);

            //Calling the nowTimeMethod method to get the current date and time.
            //this fixes it so that the date and time are always displayed in the correct format.
            nowDateTimeMethod();

            //displaying the payment details to the user and asking for confirmation before
            // adding it to the ledgerList.
            System.out.printf("\nYou are making a payment to %s for $%.2f", vendor, amount);
            System.out.print("\nAre you sure you want to proceed? (Y/N): ");
            String userPaymentInput = userInput.nextLine();

            //checking if the user wants to add the payment to the ledgerList with a simple if-else statement
            // and then adding it to the ledgerList if they confirm or cancelling the transaction if they do not confirm.
            if (userPaymentInput.equalsIgnoreCase("Y") || userPaymentInput.equalsIgnoreCase("YES")) {

                System.out.println("\nProcessing payment...");
                ledgerList.add(new AccountLedger(currentDate, currentTime, description, vendor, -amount));
                System.out.println("Payment made successfully.");

            } else if (userPaymentInput.equalsIgnoreCase("N") || userPaymentInput.equalsIgnoreCase("NO")) {

                System.out.println("\nPayment cancelled.");

            } else {

                System.out.println("\nInvalid input. Please enter Y for Yes or N for No.");

                System.out.println("\nTransaction cancelled");

                System.out.println("\nTry entering the information again.\n");
                continue;
            }

            //Giving the user the option to make another payment or exit the application.
            System.out.print("\nWould you like to make another payment? (Y/N): ");
            String userInputString = userInput.nextLine();


            //Checking if the user wants to make another payment or exit the application with a simple if-else statement.
            if (userInputString.equalsIgnoreCase("Yes") || userInputString.equalsIgnoreCase("Y")) {

                continue;
            }
            if (userInputString.equalsIgnoreCase("N") || userInputString.equalsIgnoreCase("NO")) {
                return;
            } else {

                System.out.println("\nInvalid input. Please enter Y for Yes or N for No.");

                System.out.println("\nTry entering the information again.\n");
            }
        }

    }

    //The method that I will use to display the ledger entries.
    static void displayLedger() {

        System.out.println("\nDisplaying Ledger Entries.");

        //While loop that will keep the user from exiting the ledger until they choose to exit.
        while (true) {

            System.out.println("""
                    Ledger Menu:
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
                default -> System.out.println("\nInvalid choice. Please try again.");
            }

            //Pausing the application for a moment before displaying the main menu again.
            System.out.println("\nReturning to the Ledger Menu...");
            System.out.println("Press Enter to continue.");
            userInput.nextLine();
        }
    }

    //the method that I will use to display the ledger entries.
    static void displayAllEntries() {

        counter = 0;

        //counting the number of entries in the ledger.
        for (AccountLedger ledgerEntry : ledgerList) {
            if (ledgerEntry.getAmount() != 0) {
                counter++;
            }
        }

        System.out.println("\nDisplaying all " + counter + " ledger entries.\n");

        //Checking if the ledger is empty.
        if (ledgerList.isEmpty()) {
            System.out.println("No entries in the ledger. Do a few transactions and come back to see them here.");
            return;
        }

        //Displaying the ledger entries.
        //Sort the ledger entries starting with the newest first before writing to the file
        // by date and then by timestamp.
        ledgerList.sort(Comparator.comparing(AccountLedger::getDate)
                .thenComparing(AccountLedger::getTimestamp)
                .reversed());
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

        //initializing a counter-variable to keep track of the number of deposits.
        counter = 0;

        //counting the number of deposits in the ledger.
        for (AccountLedger ledgerEntry : ledgerList) {
            if (ledgerEntry.getAmount() > 0) {
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
        //Sort the ledger entries starting with the newest first before writing to the file
        // by date and then by timestamp.
        ledgerList.sort(Comparator.comparing(AccountLedger::getDate)
                .thenComparing(AccountLedger::getTimestamp)
                .reversed());

        for (AccountLedger ledgerEntry : ledgerList) {

            if (ledgerEntry.getAmount() > 0) {
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

        //initializing a counter-variable to keep track of the number of payments.
        counter = 0;

        //counting the number of payments in the ledger.
        for (AccountLedger ledgerEntry : ledgerList) {
            if (ledgerEntry.getAmount() < 0) {
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
        //Sort the ledger entries starting with the newest first before writing to the file
        // by date and then by timestamp.
        ledgerList.sort(Comparator.comparing(AccountLedger::getDate)
                .thenComparing(AccountLedger::getTimestamp)
                .reversed());

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


        //While loop that will keep the user from exiting the reports until they choose to exit.
        while (true) {
            System.out.println("""
                    Reports Menu:
                    1) Month To Date
                    2) Previous Month
                    3) Year To Date
                    4) Previous Year
                    5) Search by Vendor
                    6) Month To Month
                    0) Back
                    """);

            System.out.print("Enter Choice: ");
            userInputString = userInput.nextLine().toUpperCase();

            //Switch statement that will handle the user's choice.
            switch (userInputString) {
                case "1" -> monthToDateFilter();
                case "2" -> previousMonthFilter();
                case "3" -> yearToDateFilter();
                case "4" -> previousYearFilter();
                case "5" -> searchByVendorFilter();
                case "6" -> monthToMonthFilter();
                case "0" -> {
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
            System.out.println("Press Enter to continue.");
            userInput.nextLine();
            System.out.println("\n");

        }
    }

    //My method for filtering ledger entries by month-to-date.
    static void monthToDateFilter() {

        //Letting the user know that they are viewing month-to-date report(s).
        System.out.println("\nDisplaying Month To Date Report(s).\n");

        //calling the nowTimeMethod method to get the current date and time.
        nowDateTimeMethod();

        //formatting the current date and time into a string.
        String monthStart = currentDateTime.format(monthFormat);
        currentDate = currentDateTime.format(dateFormat);

        //formatting the month number into a string. using my hashmap to match the month number to a string.
        System.out.println("\nDisplaying ledger entries between " + monthStart + " and " + currentDate + ".\n");

        //setting the boolean variable equal false to check if any entries are
        // found in the specified month range.
        boolean found = false;

        //using a for loop to iterate through the ledgerList and check if the date matches the monthStart.
        //Sort the ledger entries starting with the newest first before writing to the file
        // by date and then by timestamp.
        ledgerList.sort(Comparator.comparing(AccountLedger::getDate)
                .thenComparing(AccountLedger::getTimestamp)
                .reversed());

        for (AccountLedger daySearchFilter : ledgerList) {

            //getting the day number from the date string and then splitting
            // it to get the day part and then parsing it to an integer so we
            // can compare it to the dayStart and dayEnd.
            int currentYear = Integer.parseInt(daySearchFilter.getDate().split("-")[0]);
            int currentMonth = Integer.parseInt(daySearchFilter.getDate().split("-")[1]);
            int dayNumber = Integer.parseInt(daySearchFilter.getDate().split("-")[2]);
            int startDay = 1;
            int endDay = currentDateTime.getDayOfMonth();

            //checking if the year matches the current year with an if statement.
            if (currentYear == currentDateTime.getYear()) {


                //checking if the month number matches the current month with an if statement.
                if (currentDateTime.getMonthValue() == currentMonth) {

                    //checking if the day number is in the specified range.
                    if (dayNumber >= startDay && dayNumber <= endDay) {

                        //setting the boolean variable to true if any entries are found in the specified month range.
                        found = true;

                        //Printing the ledger entries that are in the specified month range.
                        System.out.printf("""
                                        %s | %s | %s | %s | %.2f
                                        """
                                , daySearchFilter.getDate()
                                , daySearchFilter.getTimestamp()
                                , daySearchFilter.getDescription()
                                , daySearchFilter.getVendor()
                                , daySearchFilter.getAmount());

                    }
                }

            }

        }
        //Letting the user know if no entries were found in the specified month range.
        if (!found) {

            System.out.println("\nNo entries found that range.");

        }

    }


    //The method that I will use to match the month number to a string.
    //I used a HashMap to store the month numbers and their corresponding strings.
    static void monthsStrings() {

        monthsAndTheirCorrespondingNumbers.put(1, "January");
        monthsAndTheirCorrespondingNumbers.put(2, "February");
        monthsAndTheirCorrespondingNumbers.put(3, "March");
        monthsAndTheirCorrespondingNumbers.put(4, "April");
        monthsAndTheirCorrespondingNumbers.put(5, "May");
        monthsAndTheirCorrespondingNumbers.put(6, "June");
        monthsAndTheirCorrespondingNumbers.put(7, "July");
        monthsAndTheirCorrespondingNumbers.put(8, "August");
        monthsAndTheirCorrespondingNumbers.put(9, "September");
        monthsAndTheirCorrespondingNumbers.put(10, "October");
        monthsAndTheirCorrespondingNumbers.put(11, "November");
        monthsAndTheirCorrespondingNumbers.put(12, "December");

    }

    //My method that I will use to display the previous month report(s)
    // and filter the ledger entries by the previous month.
    static void previousMonthFilter() {

        //Letting the user know that they are viewing previous month report(s).
        System.out.println("\nDisplaying Previous Month Report(s).\n");

        //getting the month number from the current date and then formatting it into a string.
        nowDateTimeMethod();

        //calling my HashMap method to match the month number to a string.
        monthsStrings();

        //getting the month number from the current date.
        int monthNumber = currentDateTime.getMonthValue() - 1;
        int yearNumber = currentDateTime.getYear() - 1;

        //setting the boolean variable to false to check if any entries are
        // found in the specified month range.
        boolean found = false;

        if (monthNumber == 0) {

            System.out.println("You are viewing " + yearNumber + " " + monthsAndTheirCorrespondingNumbers.get(12) + "'s entries:");

            //using a for loop to iterate through the ledgerList and check if the date matches the monthNumber.
            //Sort the ledger entries starting with the newest first before writing to the file
            // by date and then by timestamp.
            ledgerList.sort(Comparator.comparing(AccountLedger::getDate)
                    .thenComparing(AccountLedger::getTimestamp)
                    .reversed());

            for (AccountLedger daySearchFilter : ledgerList) {

                //getting the month number from the date string and then splitting
                // it to get the month part and then parsing it to an integer so we
                // can compare it to the month number on record.
                int currentYear = Integer.parseInt(daySearchFilter.getDate().split("-")[0]);
                monthNumber = Integer.parseInt(daySearchFilter.getDate().split("-")[1]);

                //checking if the year is current with an if statement.
                if (currentYear == currentDateTime.getYear() - 1 ) {


                    //checking if the month number is in the specified range.
                    if (monthNumber == 12) {

                        //setting the boolean variable to true if any entries are found in the specified month range.
                        found = true;

                        //Printing the ledger entries that are in the specified month range.
                        System.out.printf("""
                                        %s | %s | %s | %s | %.2f
                                        """
                                , daySearchFilter.getDate()
                                , daySearchFilter.getTimestamp()
                                , daySearchFilter.getDescription()
                                , daySearchFilter.getVendor()
                                , daySearchFilter.getAmount());

                    }
                }

            }

        }
        else {


            //Letting the user know which month they are viewing.
            System.out.println("You are viewing " + monthsAndTheirCorrespondingNumbers.get(monthNumber) + "'s entries:");

            //using a for loop to iterate through the ledgerList and check if the date matches the monthNumber.
            //Sort the ledger entries starting with the newest first before writing to the file
            // by date and then by timestamp.
            ledgerList.sort(Comparator.comparing(AccountLedger::getDate)
                    .thenComparing(AccountLedger::getTimestamp)
                    .reversed());

            for (AccountLedger daySearchFilter : ledgerList) {

                //getting the month number from the date string and then splitting
                // it to get the month part and then parsing it to an integer so we
                // can compare it to the month number on record.
                int currentYear = Integer.parseInt(daySearchFilter.getDate().split("-")[0]);
                monthNumber = Integer.parseInt(daySearchFilter.getDate().split("-")[1]);

                //checking if the year is current with an if statement.
                if (currentYear == currentDateTime.getYear()) {


                    //checking if the month number is in the specified range.
                    if (monthNumber == currentDateTime.getMonthValue() - 1) {

                        //setting the boolean variable to true if any entries are found in the specified month range.
                        found = true;

                        //Printing the ledger entries that are in the specified month range.
                        System.out.printf("""
                                        %s | %s | %s | %s | %.2f
                                        """
                                , daySearchFilter.getDate()
                                , daySearchFilter.getTimestamp()
                                , daySearchFilter.getDescription()
                                , daySearchFilter.getVendor()
                                , daySearchFilter.getAmount());

                    }
                }

            }
        }
            //Letting the user know if no entries were found in the specified month range.
            if (!found) {

                System.out.println("\nNo entries found in that range.");

            }


        System.out.println("\n");

    }

    //My method that I will use to display the year-to-date report(s)
    // and filter the ledger entries by the current year.
    static void yearToDateFilter() {

        //Letting the user know that they are viewing year-to-date report(s).
        System.out.println("\nDisplaying Year To Date Report.");

        //formatting the current date and time into a string.
        String yearStart = currentDateTime.format(yearFormat);
        currentDate = currentDateTime.format(dateFormat);

        //formatting the current date and time into a string.
        System.out.println("\nDisplaying ledger entries between " + yearStart + " and " + currentDate + ".\n");

        //setting the boolean variable equal false to check if any entries are true
        boolean found = false;

        //using a for loop to iterate through the ledgerList and check if the date matches the yearStart.
        //Sort the ledger entries starting with the newest first before writing to the file
        // by date and then by timestamp.
        ledgerList.sort(Comparator.comparing(AccountLedger::getDate)
                .thenComparing(AccountLedger::getTimestamp)
                .reversed());

        for (AccountLedger yearSearchFilter : ledgerList) {

            //getting the year number from the date string and then splitting it to get the
            //year part and then parsing it to an integer so we can compare it to the year number on record.
            int yearNumber = Integer.parseInt(yearSearchFilter.getDate().split("-")[0]);
            int startYear = currentDateTime.getYear();

            //checking if the year number is equal to the start year.
            if (yearNumber == startYear) {

                found = true;

                System.out.printf("""
                                %s | %s | %s | %s | %.2f
                                """
                        , yearSearchFilter.getDate()
                        , yearSearchFilter.getTimestamp()
                        , yearSearchFilter.getDescription()
                        , yearSearchFilter.getVendor()
                        , yearSearchFilter.getAmount());

            }

        }

        //letting the user know if no entries were found in the specified year range.
        if (!found) {
            System.out.println("No entries found for the specified year.");
        }

        System.out.println("\n");

    }

    static void previousYearFilter() {

        //Letting the user know that they are viewing previous year report(s).
        System.out.println("\nDisplaying Previous Year entries Report");

        //formatting the current date and time into a string.
        currentDate = currentDateTime.format(dateFormat);

        int currentYear = currentDateTime.getYear();

        //subtracting 1 from the current year to get the previous year.
        System.out.println("\nYou are viewing " + (currentYear - 1) + "'s entries:");

        //setting the boolean variable equal false to check if any entries are
        // found in the specified year range.
        boolean found = false;

        //using a for loop to iterate through the ledgerList and check if the date matches the yearStart.
        //Sort the ledger entries starting with the newest first before writing to the file
        // by date and then by timestamp.
        ledgerList.sort(Comparator.comparing(AccountLedger::getDate)
                .thenComparing(AccountLedger::getTimestamp)
                .reversed());

        for (AccountLedger daySearchFilter : ledgerList) {

            //getting the year number from the date string and then splitting
            // it to get the year part and then parsing it to an integer so we
            // can compare it to the year number on record.
            currentYear = Integer.parseInt(daySearchFilter.getDate().split("-")[0]);

            //checking if the month number is in the specified range.
            if (currentYear == currentDateTime.getYear() - 1) {

                //setting the boolean variable to true if any entries are found in the specified year range.
                found = true;

                //Printing the ledger entries that are in the specified year range.
                System.out.printf("""
                                %s | %s | %s | %s | %.2f
                                """
                        , daySearchFilter.getDate()
                        , daySearchFilter.getTimestamp()
                        , daySearchFilter.getDescription()
                        , daySearchFilter.getVendor()
                        , daySearchFilter.getAmount());

            }


        }
        //Letting the user know if no entries were found in the specified year range.
        if (!found) {

            System.out.println("\nNo entries found in that range.");

        }

        System.out.println("\n");

    }

    //The method that I will use to search for entries by vendor name.
    static void searchByVendorFilter() {

        System.out.println("\nDisplaying Search By Vendor Report.");

        //Ask the user to enter the vendor name.
        System.out.print("Enter Vendor Name: ");
        String vendor = userInput.nextLine().trim();

        System.out.println("\nDisplaying all entries for " + vendor + ".");
        boolean found = false;

        //Using a for loop to search for entries in the ledger that are for the specified vendor.
        //Sort the ledger entries starting with the newest first before writing to the file
        // by date and then by timestamp.
        ledgerList.sort(Comparator.comparing(AccountLedger::getDate)
                .thenComparing(AccountLedger::getTimestamp)
                .reversed());

        for (AccountLedger vendorName : ledgerList) {


            //checking if the vendor name matches the vendor entered by the user.
            if (vendorName.getVendor().toLowerCase().contains(vendor.toLowerCase())) {


                //setting the boolean variable to true for the specified vendor.
                found = true;

                System.out.printf("""
                                %s | %s | %s | %s | %.2f
                                """
                        , vendorName.getDate()
                        , vendorName.getTimestamp()
                        , vendorName.getDescription()
                        , vendorName.getVendor()
                        , vendorName.getAmount());

            }
        }

        //Letting the user know that no entries were found for the specified vendor.
        if (!found) {
            System.out.println("No entries found for " + vendor + ".");
        }

    }

    //The method that I will use to search for entries by month.
    static void monthToMonthFilter() {

        System.out.println("\nDisplaying Search By Month Report.");

        currentDate = currentDateTime.format(dateFormat);

        int monthStart;
        int monthEnd;
        try {

            do {
                System.out.print("\nEnter start Month(number): ");
                monthStart = userInput.nextInt();

                if (monthStart <= 0 || monthStart >= 13) {
                    System.out.println("Invalid month. Must be 1-12.");
                }

            }while (monthStart <= 0 || monthStart >= 13);

            do {

                System.out.print("Enter end Month(number): ");
                monthEnd = userInput.nextInt();
                userInput.nextLine();

                if (monthEnd <= 0 || monthEnd >= 13) {
                    System.out.println("Invalid month. Must be 1-12.");
                }

            }while (monthEnd <= 0 || monthEnd >= 13);

            if (monthStart > monthEnd) {
                System.out.println("Start month cannot be greater than end month. Please try again.");
                monthToMonthFilter();
            }


        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a number.");
            userInput.nextLine();
            return;
        }

        //setting the boolean variable too false to check if any entries are
        // found in the specified month range.
        boolean found = false;

        //calling the method that will convert the month number to a string.
        //I used the HashMap to store the month numbers and their corresponding strings.
        monthsStrings();

        //Printing the month range that the user has entered in a nice format.
        //I used the HashMap to convert the month number to a string for the
        // user to see the month names instead of just the numbers.
        if (monthStart == monthEnd) {


            System.out.println("\nDisplaying all ledger entries for "
                    + monthsAndTheirCorrespondingNumbers.get(monthStart) + ".\n");

            System.out.println("\nYou did the wrong input for the month range, but I always have one up my sleeve.");

        }
        else {

            System.out.println("\nDisplaying all ledger entries between "
                    + monthsAndTheirCorrespondingNumbers.get(monthStart) + " and "
                    + monthsAndTheirCorrespondingNumbers.get(monthEnd) + ".\n");


        }

        //Using a for loop to search for entries in the ledger that are in the specified month range.
        //Sort the ledger entries starting with the newest first before writing to the file
        // by date and then by timestamp.
        ledgerList.sort(Comparator.comparing(AccountLedger::getDate)
                .thenComparing(AccountLedger::getTimestamp)
                .reversed());

        if (monthStart == monthEnd) {

            for (AccountLedger monthSearchFilter : ledgerList) {

                //getting the month number from the date string and then splitting
                // it to get the month part and then parsing it to an integer so we
                // can compare it to the monthStart and monthEnd.
                int monthNumber = Integer.parseInt(monthSearchFilter.getDate().split("-")[1]);
                int currentYear = Integer.parseInt(monthSearchFilter.getDate().split("-")[0]);

                if (currentYear == currentDateTime.getYear()) {

                    //checking if the month number is in the specified range.
                    if (monthNumber >= monthStart && monthNumber <= monthEnd) {

                        //setting the boolean variable to true if any entries are found in the specified month range.
                        found = true;

                        //Printing the ledger entries that are in the specified month range.
                        System.out.printf("""
                                        %s | %s | %s | %s | %.2f
                                        """
                                , monthSearchFilter.getDate()
                                , monthSearchFilter.getTimestamp()
                                , monthSearchFilter.getDescription()
                                , monthSearchFilter.getVendor()
                                , monthSearchFilter.getAmount());

                    }
                }

            }
        }
        else {
            for (AccountLedger monthSearchFilter : ledgerList) {

                //getting the month number from the date string and then splitting
                // it to get the month part and then parsing it to an integer so we
                // can compare it to the monthStart and monthEnd.
                int monthNumber = Integer.parseInt(monthSearchFilter.getDate().split("-")[1]);
                int currentYear = Integer.parseInt(monthSearchFilter.getDate().split("-")[0]);

                if (currentYear == currentDateTime.getYear()) {

                    //checking if the month number is in the specified range.
                    if (monthNumber >= monthStart && monthNumber <= monthEnd) {

                        //setting the boolean variable to true if any entries are found in the specified month range.
                        found = true;

                        //Printing the ledger entries that are in the specified month range.
                        System.out.printf("""
                                        %s | %s | %s | %s | %.2f
                                        """
                                , monthSearchFilter.getDate()
                                , monthSearchFilter.getTimestamp()
                                , monthSearchFilter.getDescription()
                                , monthSearchFilter.getVendor()
                                , monthSearchFilter.getAmount());

                    }
                }

            }
        }
        //Letting the user know if no entries were found in the specified month range.
        if (!found) {

            System.out.println("\nNo entries found that range.");

            System.out.println("\nWanna give it another try? (Y/N): ");
            String userInputString = userInput.nextLine();

            if (userInputString.equalsIgnoreCase("Y") || userInputString.equalsIgnoreCase("yes")) {

                monthStart = 0;
                monthEnd = 0;
                monthToMonthFilter();
            } else {
                System.out.println("\nReturning to the Reports Menu...");
            }

        }

    }
    public static void nowDateTimeMethod() {

        //below I'm getting the current date and time and formatting them into a string.
        //so I can add them to the deposit automatically as the user enters the details.
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("America/Chicago"));
        currentTime = now.format(timeFormat);
        currentDate = now.format(dateFormat);
    }
}