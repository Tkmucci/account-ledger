package com.pluralsight;

public class AccountLedger {

    private String description;
    private double amount;
    private String vendor;
    private String timestamp;
    private String date;


    public AccountLedger(String desc, double amt, String vend, String time, String date) {

        this.description = desc;
        this.amount = amt;
        this.vendor = vend;
        this.timestamp = time;
        this.date = date;

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}