package ui.controllers;

import Models.Child;
import Models.Notification;
import Models.Parent;
import Models.Registration;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import repositories.NotificationRepository;
import services.ChildService;
import services.RegistrationService;

import java.util.List;

/**
 * Contrôleur du tableau de bord parent.
 *
 * CORRECTIONS :
 * 1. Les fx:id (childrenListView, registrationsListView, notificationsListView)
 *    sont maintenant présents dans dashboard_parent.fxml → les ListViews ne sont plus null.
 * 2. showChildren() affiche VRAIMENT les données dans la ListView (pas juste en console).
 * 3. openRegistration() ouvre la fenêtre en modal et rafraîchit après fermeture.
 * 4. openPayment() passe le parent au PaymentController (setCurrentParent existe maintenant).
 */
public class DashboardParentController {

    @FXML private Label welcomeLabel;
    @FXML private ListView<String> childrenListView;
    @FXML private ListView<String> registrationsListView;
    @FXML private ListView<String> notificationsListView;
    @FXML private Label infoLabel;

    private Parent currentUser;
    private ChildService childService;
    private RegistrationService registrationService;
    private NotificationRepository notificationRepository;

    public DashboardParentController() {
        childService = new ChildService();
        registrationService = new RegistrationService();
        notificationRepository = new NotificationRepository();
    }

    @FXML
    public void initialize() {
        if (welcomeLabel != null) {
            welcomeLabel.setText("Bienvenue !");
        }
    }

    /**
     * Appelé par LoginController après le chargement du FXML.
     * Initialise les données et charge les enfants automatiquement.
     */
    public void setCurrentUser(Parent parent) {
        this.currentUser = parent;
        if (welcomeLabel != null) {
            welcomeLabel.setText("Bienvenue, " + parent.getFirstName()
                + " " + parent.getLastName() + " !");
        }
        showChildren();
    }

    /**
     * Affiche la liste des enfants du parent dans la ListView.
     * CORRECTION : avant cette méthode n'était pas branchée sur le bon fx:id.
     */
    @FXML
    public void showChildren() {
        if (currentUser == null) return;

         List<Child> children = currentUser.getChildren();  // Utiliser le parent
         // affiche les enfants ✅

        if (childrenListView != null) {
            childrenListView.getItems().clear();
            if (children.isEmpty()) {
                childrenListView.getItems().add("Aucun enfant inscrit pour l'instant.");
            } else {
                for (Child c : children) {
                    String info = "• " + c.getFirstName() + " " + c.getLastName();
                    if (c.getLevel() != null && !c.getLevel().isEmpty()) {
                        info += "  —  Niveau : " + c.getLevel();
                    }
                    if (c.getAge() > 0) {
                        info += "  —  " + c.getAge() + " ans";
                    }
                    childrenListView.getItems().add(info);
                }
            }
        }

        if (infoLabel != null) {
            infoLabel.setText(children.size() + " enfant(s) trouvé(s).");
        }
    }

    /**
     * Affiche les inscriptions du parent connecté.
     */
    @FXML
    public void showRegistrations() {
        if (currentUser == null) return;

        List<Registration> registrations =
            registrationService.getRegistrationsByParent(currentUser.getId());

        if (registrationsListView != null) {
            registrationsListView.getItems().clear();
            if (registrations.isEmpty()) {
                registrationsListView.getItems().add("Aucune inscription trouvée.");
            } else {
                for (Registration r : registrations) {
                    String info = "📋 Inscription #" + r.getId()
                        + "  —  Enfant ID: " + r.getChildId()
                        + "  —  Créneau ID: " + r.getTimeSlotId()
                        + "  —  " + r.getRegistrationDate();
                    if (r.getPayment() != null) {
                        info += "  |  Payé: "
                            + String.format("%.2f", r.getPayment().getPaidAmount())
                            + " / "
                            + String.format("%.2f", r.getPayment().getTotalAmount()) + " €";
                    }
                    registrationsListView.getItems().add(info);
                }
            }
        }

        if (infoLabel != null) {
            infoLabel.setText(registrations.size() + " inscription(s) trouvée(s).");
        }
    }

    /**
     * Affiche les notifications du parent connecté.
     */
    @FXML
    public void showNotifications() {
        if (currentUser == null) return;

        List<Notification> notifications =
            notificationRepository.findByParentId(currentUser.getId());

        if (notificationsListView != null) {
            notificationsListView.getItems().clear();
            if (notifications.isEmpty()) {
                notificationsListView.getItems().add("Aucune notification.");
            } else {
                for (Notification n : notifications) {
                    String status = n.isSent() ? "✓ Envoyée" : "⏳ En attente";
                    String date = n.getSentDate() != null
                        ? " le " + n.getSentDate().toLocalDate() : "";
                    notificationsListView.getItems().add(
                        "[" + status + date + "] " + n.getMessage()
                    );
                }
            }
        }
    }

    /**
     * Ouvre la fenêtre d'inscription en modal.
     * Rafraîchit les enfants et inscriptions après fermeture.
     */
    @FXML
    public void openRegistration() {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/ui/views/registration.fxml")
            );
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(loader.load(), 560, 620));
            stage.setTitle("Inscription d'un enfant");

            RegistrationController controller = loader.getController();
            controller.setCurrentParent(currentUser);

            stage.showAndWait(); // Modal : attend la fermeture

            // Rafraîchit TOUTES les données après inscription
            showChildren();
            showRegistrations();
            showNotifications();  // ✅ AJOUT CRUCIAL
            if (infoLabel != null) infoLabel.setText("Données mises à jour.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Ouvre la fenêtre de paiement en modal.
     * CORRECTION : transmet le parent au PaymentController via setCurrentParent().
     */
    @FXML
    public void openPayment() {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/ui/views/payment.fxml")
            );
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(loader.load(), 520, 460));
            stage.setTitle("Gestion des paiements");

            PaymentController controller = loader.getController();
            controller.setCurrentParent(currentUser); // CORRECTION : méthode désormais existante

            stage.showAndWait();

            // Rafraîchit les inscriptions après paiement
            showRegistrations();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Déconnecte et revient à la page de connexion.
     */
    @FXML
    public void logout() {
        try {
            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/ui/views/login.fxml")
            );
            stage.setScene(new Scene(loader.load(), 600, 400));
            stage.setTitle("Connexion");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
