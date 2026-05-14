package main.java.ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class DashboardAdministratorController {

    @FXML
    private Label adminLabel;

    @FXML
    public void initialize() {

        adminLabel.setText(
                "Administrator Dashboard"
        );
    }
}