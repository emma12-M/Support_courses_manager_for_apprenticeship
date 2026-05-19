package ui.controllers;

import Models.Child;
import Models.Parent;
import Models.Payment;
import Models.TimeSlot;
import enums.PaymentType;
import exceptions.CapacityException;
import facades.ManagementFacade;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import services.ChildService;
import services.TimeSlotService;
import singleton.AppConfig;

import java.util.List;

/**
 * Contrôleur de la vue d'inscription (registration.fxml).
 *
 * CORRECTIONS apportées :
 * 1. childService.addChildToParent() reçoit maintenant le parent pour fixer parentId
 * 2. managementFacade.completeRegistration() reçoit le parent pour transmettre parentId
 * 3. Validation du nombre de versements avec la constante AppConfig.getMaxInstallments()
 * 4. Génération d'IDs cohérente (évite les collisions)
 */
public class RegistrationController {

    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField levelField;
    @FXML private ComboBox<TimeSlot> timeSlotComboBox;
    @FXML private ComboBox<String> paymentTypeComboBox;
    @FXML private TextField totalAmountField;
    @FXML private TextField installmentCountField;
    @FXML private Label statusLabel;
    @FXML private Label installmentLabel;

    private ManagementFacade managementFacade;
    private TimeSlotService timeSlotService;
    private ChildService childService;

    // Le parent connecté — transmis par DashboardParentController
    private Parent currentParent;

    public RegistrationController() {
        managementFacade = new ManagementFacade();
        timeSlotService = new TimeSlotService();
        childService = new ChildService();
    }

    @FXML
    public void initialize() {

        // Charge les créneaux disponibles dans le ComboBox
        List<TimeSlot> slots = timeSlotService.getAllTimeSlots();
        timeSlotComboBox.setItems(FXCollections.observableArrayList(slots));

        // Remplit le ComboBox des types de paiement
        paymentTypeComboBox.setItems(FXCollections.observableArrayList(
            "Paiement en une fois",
            "Paiement fractionné (max " + AppConfig.getInstance().getMaxInstallments() + ")"
        ));
        paymentTypeComboBox.setValue("Paiement en une fois");

        // Cache le champ du nombre de versements par défaut
        if (installmentCountField != null) installmentCountField.setVisible(false);
        if (installmentLabel != null) installmentLabel.setVisible(false);

        // Affiche/cache le champ de versements selon le choix
        paymentTypeComboBox.setOnAction(e -> {
        boolean isSplit = paymentTypeComboBox.getValue() != null
            && paymentTypeComboBox.getValue().startsWith("Paiement fractionné");
        
        if (installmentCountField != null) {
            installmentCountField.setVisible(isSplit);
            installmentCountField.setManaged(isSplit);  // 🔑 CLEF MANQUANTE !
        }
        
        if (installmentLabel != null) {
            installmentLabel.setVisible(isSplit);
            installmentLabel.setManaged(isSplit);       // 🔑 CLEF MANQUANTE !
        }
    });
    }

    /**
     * Appelé par DashboardParentController pour transmettre le parent connecté.
     */
    public void setCurrentParent(Parent parent) {
        this.currentParent = parent;
    }

    @FXML
    public void handleRegistration() {

        String firstName = firstNameField.getText().trim();
        String lastName  = lastNameField.getText().trim();
        String level     = (levelField != null) ? levelField.getText().trim() : "";
        TimeSlot selectedSlot = timeSlotComboBox.getValue();
        String paymentChoice  = paymentTypeComboBox.getValue();
        String amountText     = totalAmountField.getText().trim();

        // Validation
        if (firstName.isEmpty() || lastName.isEmpty()) {
            showError("Le prénom et le nom de l'enfant sont obligatoires.");
            return;
        }
        if (selectedSlot == null) {
            showError("Veuillez sélectionner un créneau.");
            return;
        }
        if (amountText.isEmpty()) {
            showError("Veuillez entrer un montant.");
            return;
        }

        double totalAmount;
        try {
            totalAmount = Double.parseDouble(amountText);
            if (totalAmount <= 0) {
                showError("Le montant doit être supérieur à 0.");
                return;
            }
        } catch (NumberFormatException e) {
            showError("Le montant doit être un nombre (ex: 120.50).");
            return;
        }

        // Type de paiement et versements
        PaymentType paymentType;
        int installmentCount;
        int maxInstallments = AppConfig.getInstance().getMaxInstallments();

        if (paymentChoice != null && paymentChoice.startsWith("Paiement fractionné")) {
            paymentType = PaymentType.SPLIT_PAYMENT;
            String countText = installmentCountField.getText().trim();
            try {
                installmentCount = Integer.parseInt(countText);
                if (installmentCount < 2 || installmentCount > maxInstallments) {
                    showError("Le nombre de versements doit être entre 2 et " + maxInstallments + ".");
                    return;
                }
            } catch (NumberFormatException e) {
                showError("Entrez un nombre entier entre 2 et " + maxInstallments + ".");
                return;
            }
        } else {
            paymentType = PaymentType.SINGLE_PAYMENT;
            installmentCount = 1;
        }

        // Crée l'enfant
        Child child = new Child();
        child.setFirstName(firstName);
        child.setLastName(lastName);
        if (!level.isEmpty()) child.setLevel(level);

        // Ajoute l'enfant au parent (définit parentId + sauvegarde dans JSON)
        if (currentParent != null) {
            childService.addChildToParent(currentParent, child);
        } else {
            child.setId((int)(System.currentTimeMillis() % 100000));
        }

        // Crée le paiement
        int paymentId = (int)(System.currentTimeMillis() % 100000) + 1;
        Payment payment = new Payment(paymentId, totalAmount, installmentCount, paymentType);

        // Appelle la Facade pour tout orchestrer
        try {
            Parent parent = (currentParent != null)
                ? currentParent
                : new Parent(0, "Parent", "Inconnu", "inconnu@email.com", "");

            managementFacade.completeRegistration(parent, child, selectedSlot, payment);

            if (statusLabel != null) {
                statusLabel.setText("✓ Inscription réussie ! "
                    + child.getFirstName() + " est inscrit(e) au créneau "
                    + selectedSlot.getSubject() + ".");
                statusLabel.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
            }

            // Rafraîchit le ComboBox
            timeSlotComboBox.setItems(FXCollections.observableArrayList(
                timeSlotService.getAllTimeSlots()
            ));

            // Vide les champs
            firstNameField.clear();
            lastNameField.clear();
            if (levelField != null) levelField.clear();
            totalAmountField.clear();
            if (installmentCountField != null) installmentCountField.clear();

        } catch (CapacityException e) {
            showError("Ce créneau est complet. Veuillez en choisir un autre.");
        }
    }

    @FXML
    public void handleClose() {
        Stage stage = (Stage) firstNameField.getScene().getWindow();
        stage.close();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }
}

