package interfaces;

public interface PaymentStrategy {

    double calculatePayment(double totalAmount,
                            int installmentCount);
}

