package main.java.ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class DashboardParentController {

    @FXML
    private Label welcomeLabel;

    @FXML
    public void initialize() {

        welcomeLabel.setText(
                "Parent Dashboard"
        );
    }
}