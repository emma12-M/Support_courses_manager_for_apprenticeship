package main.java.ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import main.java.services.PaymentService;

public class PaymentController {

    private PaymentService paymentService;

    public PaymentController() {

        paymentService =
                new PaymentService();
    }

    @FXML
    public void handlePayment() {

        Alert alert =
                new Alert(
                        Alert.AlertType.INFORMATION
                );

        alert.setContentText(
                "Payment processed"
        );

        alert.show();
    }
}