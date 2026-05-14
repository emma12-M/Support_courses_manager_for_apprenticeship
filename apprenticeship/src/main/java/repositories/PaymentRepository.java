package main.java.repositories;

import main.java.Models.Payment;

import java.util.ArrayList;
import java.util.List;

public class PaymentRepository {

    private List<Payment> payments;

    public PaymentRepository() {

        payments = new ArrayList<>();
    }

    public void save(Payment payment) {

        payments.add(payment);
    }

    public List<Payment> findAll() {

        return payments;
    }
}