package factories;

import enums.PaymentType;
import interfaces.PaymentStrategy;
import strategies.SinglePayment;
import strategies.SplitPayment;

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

