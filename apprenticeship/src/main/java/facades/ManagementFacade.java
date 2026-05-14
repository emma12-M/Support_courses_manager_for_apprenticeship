package main.java.facades;

import main.java.Models.Child;
import main.java.Models.Parent;
import main.java.Models.Payment;
import main.java.Models.Registration;
import main.java.Models.TimeSlot;
import main.java.services.NotificationService;
import main.java.services.PaymentService;
import main.java.services.RegistrationService;

public class ManagementFacade {

    private RegistrationService registrationService;

    private PaymentService paymentService;

    private NotificationService notificationService;

    public ManagementFacade() {

        registrationService =
                new RegistrationService();

        paymentService =
                new PaymentService();

        notificationService =
                new NotificationService();
    }

    public void completeRegistration(Parent parent,
                                     Child child,
                                     TimeSlot timeSlot,
                                     Payment payment) {

        Registration registration =
                registrationService.registerChild(
                        child,
                        timeSlot,
                        payment
                );

        paymentService.processPayment(payment);

        notificationService.notifyObservers(
                "Registration completed successfully"
        );

        System.out.println(
                "Full registration completed"
        );
    }
}