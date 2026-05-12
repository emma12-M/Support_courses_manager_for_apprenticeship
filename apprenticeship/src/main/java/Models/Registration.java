package models;

import java.time.LocalDate;

public class Registration {

    private int id;

    private Child child;

    private TimeSlot timeSlot;

    private LocalDate registrationDate;

    private Payment payment;

    public Registration() {
    }

    public Registration(int id,
                        Child child,
                        TimeSlot timeSlot,
                        LocalDate registrationDate,
                        Payment payment) {

        this.id = id;
        this.child = child;
        this.timeSlot = timeSlot;
        this.registrationDate = registrationDate;
        this.payment = payment;
    }

    // GETTERS & SETTERS
}
