package main.java.strategies;

import main.java.interfaces.PaymentStrategy;

public class SplitPayment implements PaymentStrategy {

	@Override
    public double calculatePayment(double totalAmount,
                                   int installmentCount) {

        if(installmentCount <= 0) {
            return totalAmount;
        }

        return totalAmount / installmentCount;
    }
}
