package models;

import enums.PaymentType;

public class Payment {

    private int id;

    private double totalAmount;

    private double paidAmount;

    private double remainingAmount;

    private int installmentCount;

    private PaymentType paymentType;

    public Payment() {
    }

    public Payment(int id,
                   double totalAmount,
                   int installmentCount,
                   PaymentType paymentType) {

        this.id = id;
        this.totalAmount = totalAmount;
        this.installmentCount = installmentCount;
        this.paymentType = paymentType;

        this.paidAmount = 0;
        this.remainingAmount = totalAmount;
    }

    public void makePayment(double amount) {

        paidAmount += amount;

        remainingAmount -= amount;
    }

    public boolean isCompleted() {
        return remainingAmount <= 0;
    }

    // GETTERS & SETTERS
}