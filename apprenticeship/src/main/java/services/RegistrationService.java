package main.java.services;

import main.java.Models.Child;
import main.java.Models.Payment;
import main.java.Models.Registration;
import main.java.Models.TimeSlot;
import main.java.repositories.RegistrationRepository;

import java.time.LocalDate;

public class RegistrationService {

    private RegistrationRepository
            registrationRepository;

    public RegistrationService() {

        registrationRepository =
                new RegistrationRepository();
    }

    public Registration registerChild(
            Child child,
            TimeSlot timeSlot,
            Payment payment) {

        timeSlot.register();

        Registration registration =
                new Registration(
                        1,
                        child,
                        timeSlot,
                        LocalDate.now(),
                        payment
                );

        registrationRepository.save(
                registration
        );

        return registration;
    }
}