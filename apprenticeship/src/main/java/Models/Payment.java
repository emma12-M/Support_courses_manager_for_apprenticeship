package Models;

import enums.PaymentType;

public class Payment {

    private int id;

    private double totalAmount;

    private double paidAmount;

    private double remainingAmount;

    private int installmentCount;

    private int completedInstallments;

    private int remainingInstallments;

    private PaymentType paymentType;

    private boolean completed;

    public Payment() {
        completed = false;
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
        
        // Marque le paiement comme complet si plus rien à payer
        if(remainingAmount == 0) {
            completed = true;
        }
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
	
	public boolean isCompleted() {
		return completed;
	}
	
	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public void setPaidAmount(double paidAmount) {
		this.paidAmount = paidAmount;
	}

	public void setRemainingAmount(double remainingAmount) {
		this.remainingAmount = remainingAmount;
	}

	public void setInstallmentCount(int installmentCount) {
		this.installmentCount = installmentCount;
	}

	public void setCompletedInstallments(int completedInstallments) {
		this.completedInstallments = completedInstallments;
	}

	public void setRemainingInstallments(int remainingInstallments) {
		this.remainingInstallments = remainingInstallments;
	}

	public void setPaymentType(PaymentType paymentType) {
		this.paymentType = paymentType;
	}
}

