package main.java.Models;

import main.java.enums.PaymentType;

public class Payment {

    private int id;

    private double totalAmount;

    private double paidAmount;

    private double remainingAmount;

    private int installmentCount;

    private int completedInstallments;

    private int remainingInstallments;

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

        this.completedInstallments = 0;

        this.remainingInstallments = installmentCount;
    }

    public void makePayment(double amount) {

        paidAmount += amount;

        remainingAmount -= amount;

        completedInstallments++;

        remainingInstallments--;

        if(remainingAmount < 0) {
            remainingAmount = 0;
        }

        if(remainingInstallments < 0) {
            remainingInstallments = 0;
        }
    }

    public boolean isCompleted() {

        return remainingAmount <= 0;
    }

	public int getId() {
		return id;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public double getPaidAmount() {
		return paidAmount;
	}

	public double getRemainingAmount() {
		return remainingAmount;
	}

	public int getInstallmentCount() {
		return installmentCount;
	}

	public int getCompletedInstallments() {
		return completedInstallments;
	}

	public int getRemainingInstallments() {
		return remainingInstallments;
	}

	public PaymentType getPaymentType() {
		return paymentType;
	}
}