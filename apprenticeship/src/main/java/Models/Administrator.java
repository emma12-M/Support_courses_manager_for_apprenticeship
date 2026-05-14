package main.java.Models;

public class Administrator extends User {

    public Administrator() {
    }

    public Administrator(int id,
                         String firstName,
                         String lastName,
                         String email,
                         String password) {

        super(id, firstName, lastName, email, password);
    }

    @Override
    public void displayMenu() {
        System.out.println("Administrator Dashboard");
    }
}
