package main.java.strategies;

import main.java.interfaces.PaymentStrategy;

public class SinglePayment implements PaymentStrategy {

    @Override
    public double calculatePayment(double totalAmount,
                                   int installmentCount) {

        return totalAmount;
    }
}
