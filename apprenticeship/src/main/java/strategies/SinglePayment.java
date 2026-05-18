package strategies;

import interfaces.PaymentStrategy;

public class SinglePayment implements PaymentStrategy {

    @Override
    public double calculatePayment(double totalAmount,
                                   int installmentCount) {

        return totalAmount;
    }
}


