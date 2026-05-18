package strategies;

import interfaces.PaymentStrategy;

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


