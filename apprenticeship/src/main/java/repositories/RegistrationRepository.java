package main.java.repositories;

import main.java.Models.Registration;

import java.util.ArrayList;
import java.util.List;

public class RegistrationRepository {

    private List<Registration> registrations;

    public RegistrationRepository() {

        registrations = new ArrayList<>();
    }

    public void save(Registration registration) {

        registrations.add(registration);
    }

    public List<Registration> findAll() {

        return registrations;
    }
}