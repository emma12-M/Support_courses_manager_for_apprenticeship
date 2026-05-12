package models;

import java.time.LocalDate;

public class Child {

    private int id;

    private String firstName;

    private String lastName;

    private int age;

    private String level;

    private LocalDate birthDate;

    public Child() {
    }

    public Child(int id,
                 String firstName,
                 String lastName,
                 int age,
                 String level,
                 LocalDate birthDate) {

        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.level = level;
        this.birthDate = birthDate;
    }

    // GETTERS & SETTERS
}