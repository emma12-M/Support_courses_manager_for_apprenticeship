package main.java.factories;

import main.java.enums.UserRole;
import  main.java.Models.Administrator;
import main.java.Models.Parent;
import main.java.Models.User;

public class UserFactory {

    public static User createUser(UserRole role,
                                  int id,
                                  String firstName,
                                  String lastName,
                                  String email,
                                  String password) {

        switch (role) {

            case PARENT:

                return new Parent(
                        id,
                        firstName,
                        lastName,
                        email,
                        password
                );

            case ADMINISTRATOR:

                return new Administrator(
                        id,
                        firstName,
                        lastName,
                        email,
                        password
                );

            default:

                throw new IllegalArgumentException(
                        "Invalid user role"
                );
        }
    }
}
