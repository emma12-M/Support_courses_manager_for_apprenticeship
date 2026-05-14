package main.java.ui.controllers;

import main.java.facades.ManagementFacade;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;

public class RegistrationController {

    private ManagementFacade
            managementFacade;

    public RegistrationController() {

        managementFacade =
                new ManagementFacade();
    }

    @FXML
    public void handleRegistration() {

        Alert alert =
                new Alert(
                        Alert.AlertType.INFORMATION
                );

        alert.setContentText(
                "Registration completed"
        );

        alert.show();
    }
}