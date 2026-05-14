package main.java.factories;

import main.java.enums.PaymentType;
import main.java.interfaces.PaymentStrategy;
import main.java.strategies.SinglePayment;
import main.java.strategies.SplitPayment;

public class PaymentFactory {

    public static PaymentStrategy createPaymentStrategy(
            PaymentType paymentType) {

        switch (paymentType) {

            case SINGLE_PAYMENT:

                return new SinglePayment();

            case SPLIT_PAYMENT:

                return new SplitPayment();

            default:

                throw new IllegalArgumentException(
                        "Invalid payment type"
                );
        }
    }
}