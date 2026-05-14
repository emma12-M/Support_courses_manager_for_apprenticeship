package main.java.services;

import main.java.Models.Parent;
import main.java.repositories.ParentRepository;

public class AuthService {

    private ParentRepository parentRepository;

    public AuthService() {

        parentRepository =
                new ParentRepository();
    }

    public boolean login(String email,
                         String password) {

        Parent parent =
                parentRepository.findByEmail(email);

        if(parent == null) {

            return false;
        }

        return parent.getPassword()
                .equals(password);
    }
}