package main.java.services;

import main.java.factories.PaymentFactory;
import main.java.interfaces.PaymentStrategy;
import main.java.Models.Payment;

public class PaymentService {

    private PaymentStrategy paymentStrategy;

    public PaymentService() {
    }

    public void setPaymentStrategy(
            PaymentStrategy paymentStrategy) {

        this.paymentStrategy =
                paymentStrategy;
    }

    public double processPayment(
            Payment payment) {

        paymentStrategy =
                PaymentFactory
                .createPaymentStrategy(
                        payment.getPaymentType()
                );

        double amount =
                paymentStrategy
                .calculatePayment(
                        payment.getTotalAmount(),
                        payment.getInstallmentCount()
                );

        payment.makePayment(amount);

        return amount;
    }
}