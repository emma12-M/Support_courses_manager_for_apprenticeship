package main.java.ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import main.java.services.AuthService;

public class LoginController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    private AuthService authService;

    public LoginController() {

        authService =
                new AuthService();
    }

    @FXML
    public void handleLogin() {

        String email =
                emailField.getText();

        String password =
                passwordField.getText();

        boolean success =
                authService.login(
                        email,
                        password
                );

        if(success) {

            Alert alert =
                    new Alert(
                            Alert.AlertType.INFORMATION
                    );

            alert.setContentText(
                    "Login successful"
            );

            alert.show();
        }
        else {

            Alert alert =
                    new Alert(
                            Alert.AlertType.ERROR
                    );

            alert.setContentText(
                    "Invalid credentials"
            );

            alert.show();
        }
    }
}