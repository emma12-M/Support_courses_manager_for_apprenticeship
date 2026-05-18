package factories;

import enums.UserRole;
import  Models.Administrator;
import Models.Parent;
import Models.User;

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


