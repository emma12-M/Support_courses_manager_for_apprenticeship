package main.java.interfaces;

public interface PaymentStrategy {

    double calculatePayment(double totalAmount,
                            int installmentCount);
}