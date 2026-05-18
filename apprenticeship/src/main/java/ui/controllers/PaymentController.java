package ui.controllers;

import Models.Parent;
import Models.Payment;
import Models.Registration;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import repositories.PaymentRepository;
import services.RegistrationService;

import java.util.List;

/**
 * Contrôleur de la vue de paiement (payment.fxml).
 *
 * CORRECTIONS :
 * 1. Ajout de setCurrentParent() — appelé par DashboardParentController
 *    (avant : méthode absente → NullPointerException)
 * 2. Affichage des inscriptions et paiements du parent connecté
 * 3. handlePayment() effectue vraiment un versement et sauvegarde dans payment.json
 */
public class PaymentController {

    @FXML private Label titleLabel;
    @FXML private ComboBox<String> registrationComboBox;
    @FXML private Label paymentInfoLabel;
    @FXML private Label totalLabel;
    @FXML private Label paidLabel;
    @FXML private Label remainingLabel;
    @FXML private Label statusLabel;
    @FXML private Button payButton;
    @FXML private Button closeButton;

    private Parent currentParent;
    private List<Registration> registrations;
    private RegistrationService registrationService;
    private PaymentRepository paymentRepository;

    public PaymentController() {
        registrationService = new RegistrationService();
        paymentRepository = new PaymentRepository();
    }

    @FXML
    public void initialize() {
        if (titleLabel != null) {
            titleLabel.setText("Gestion des paiements");
        }
    }

    /**
     * CORRECTION : méthode manquante — appelée par DashboardParentController
     * après le chargement du FXML.
     */
    public void setCurrentParent(Parent parent) {
        this.currentParent = parent;
        loadRegistrations();
    }

    /**
     * Charge les inscriptions du parent et les affiche dans le ComboBox.
     */
    private void loadRegistrations() {
        if (currentParent == null) return;

        registrations = registrationService.getRegistrationsByParent(currentParent.getId());

        if (registrations.isEmpty()) {
            if (registrationComboBox != null) {
                registrationComboBox.setItems(FXCollections.observableArrayList(
                    "Aucune inscription trouvée"
                ));
            }
            if (payButton != null) payButton.setDisable(true);
        } else {
            java.util.List<String> labels = new java.util.ArrayList<>();
            for (Registration r : registrations) {
                Payment p = r.getPayment();
                String label = "Inscription #" + r.getId()
                    + " — Enfant ID: " + r.getChildId()
                    + " | Créneau ID: " + r.getTimeSlotId();
                if (p != null) {
                    label += " | Reste: " + String.format("%.2f", p.getRemainingAmount()) + " €";
                }
                labels.add(label);
            }
            if (registrationComboBox != null) {
                registrationComboBox.setItems(FXCollections.observableArrayList(labels));
                registrationComboBox.setOnAction(e -> updatePaymentInfo());
                registrationComboBox.getSelectionModel().selectFirst();
                updatePaymentInfo();
            }
        }
    }

    /**
     * Met à jour les labels d'information quand on sélectionne une inscription.
     */
    private void updatePaymentInfo() {
        int idx = registrationComboBox.getSelectionModel().getSelectedIndex();
        if (idx < 0 || idx >= registrations.size()) return;

        Payment p = registrations.get(idx).getPayment();
        if (p == null) return;

        if (totalLabel != null)     totalLabel.setText("Montant total : " + String.format("%.2f", p.getTotalAmount()) + " €");
        if (paidLabel != null)      paidLabel.setText("Déjà payé : " + String.format("%.2f", p.getPaidAmount()) + " €");
        if (remainingLabel != null) remainingLabel.setText("Reste à payer : " + String.format("%.2f", p.getRemainingAmount()) + " €");

        if (payButton != null) {
            payButton.setDisable(p.isCompleted());
        }
        if (paymentInfoLabel != null) {
            String type = p.getPaymentType() != null ? p.getPaymentType().name() : "?";
            paymentInfoLabel.setText("Type : " + type
                + " | Versements : " + p.getCompletedInstallments()
                + "/" + p.getInstallmentCount());
        }
    }

    /**
     * Effectue un versement sur l'inscription sélectionnée.
     * CORRECTION : persiste le paiement dans payment.json via PaymentRepository.
     */
    @FXML
    public void handlePayment() {
        int idx = (registrationComboBox != null)
            ? registrationComboBox.getSelectionModel().getSelectedIndex() : -1;

        if (idx < 0 || idx >= registrations.size()) {
            showAlert(Alert.AlertType.WARNING, "Sélectionnez une inscription.");
            return;
        }

        Payment payment = registrations.get(idx).getPayment();
        if (payment == null) {
            showAlert(Alert.AlertType.ERROR, "Aucun paiement associé à cette inscription.");
            return;
        }

        if (payment.isCompleted()) {
            showAlert(Alert.AlertType.INFORMATION, "Ce paiement est déjà soldé.");
            return;
        }

        // Calcule le montant du prochain versement
        double installmentAmount = payment.getTotalAmount() / payment.getInstallmentCount();
        if (installmentAmount > payment.getRemainingAmount()) {
            installmentAmount = payment.getRemainingAmount();
        }

        // Enregistre le versement
        payment.makePayment(installmentAmount);

        // Sauvegarde dans payment.json
        paymentRepository.update(payment);

        if (statusLabel != null) {
            statusLabel.setText(
                "✓ Versement de " + String.format("%.2f", installmentAmount)
                + " € enregistré. Reste : "
                + String.format("%.2f", payment.getRemainingAmount()) + " €"
            );
            statusLabel.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
        }

        // Rafraîchit l'affichage
        updatePaymentInfo();
    }

    @FXML
    public void handleClose() {
        try {
            Stage stage = (Stage) (titleLabel != null
                ? titleLabel.getScene().getWindow()
                : closeButton.getScene().getWindow());
            stage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }
}
