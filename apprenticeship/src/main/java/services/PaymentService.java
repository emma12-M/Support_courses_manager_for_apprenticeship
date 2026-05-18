package services;

import factories.PaymentFactory;
import interfaces.PaymentStrategy;
import Models.Payment;
import repositories.PaymentRepository;

public class PaymentService {

    private PaymentStrategy paymentStrategy;
    private PaymentRepository paymentRepository;

    public PaymentService() {
        paymentRepository = new PaymentRepository();
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
        
        // CORRECTION : Sauvegarde le paiement en JSON
        paymentRepository.save(payment);

        return amount;
    }
}

