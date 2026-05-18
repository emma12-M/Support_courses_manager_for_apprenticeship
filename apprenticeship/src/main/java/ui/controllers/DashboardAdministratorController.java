package ui.controllers;

import Models.Administrator;
import Models.Child;
import Models.Registration;
import Models.TimeSlot;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import repositories.ChildRepository;
import repositories.NotificationRepository;
import services.RegistrationService;
import services.TimeSlotService;

import java.util.List;

/**
 * Contrôleur du tableau de bord administrateur.
 *
 * CORRECTIONS / AMÉLIORATIONS :
 * 1. Affichage de toutes les inscriptions
 * 2. Affichage de tous les créneaux avec leur état (disponible/complet)
 * 3. Affichage de tous les enfants inscrits
 * 4. Affichage de toutes les notifications envoyées
 * 5. Navigation vers la page d'inscription (pour tester)
 */
public class DashboardAdministratorController {

    @FXML private Label adminLabel;
    @FXML private ListView<String> registrationsListView;
    @FXML private ListView<String> timeSlotsListView;
    @FXML private ListView<String> childrenListView;
    @FXML private ListView<String> notificationsListView;
    @FXML private Label statsLabel;

    private Administrator currentUser;
    private RegistrationService registrationService;
    private TimeSlotService timeSlotService;
    private ChildRepository childRepository;
    private NotificationRepository notificationRepository;

    public DashboardAdministratorController() {
        registrationService = new RegistrationService();
        timeSlotService = new TimeSlotService();
        childRepository = new ChildRepository();
        notificationRepository = new NotificationRepository();
    }

    @FXML
    public void initialize() {
        if (adminLabel != null) adminLabel.setText("Tableau de bord Administrateur");
    }

    /**
     * Appelé par LoginController après le chargement du FXML.
     */
    public void setCurrentUser(Administrator admin) {
        this.currentUser = admin;
        if (adminLabel != null) {
            adminLabel.setText("Administrateur : " + admin.getFirstName() + " " + admin.getLastName());
        }
        loadAllData();
    }

    /**
     * Charge toutes les données au démarrage.
     */
    private void loadAllData() {
        showRegistrations();
        showTimeSlots();
        showChildren();
        showNotifications();
        updateStats();
    }

    /** Affiche toutes les inscriptions. */
    @FXML
    public void showRegistrations() {
        if (registrationsListView == null) return;
        List<Registration> all = registrationService.getAllRegistrations();
        if (all.isEmpty()) {
            registrationsListView.setItems(FXCollections.observableArrayList("Aucune inscription."));
        } else {
            java.util.List<String> lines = new java.util.ArrayList<>();
            for (Registration r : all) {
                String line = "📋 Inscription #" + r.getId()
                    + " — Enfant ID: " + r.getChildId()
                    + " — Créneau ID: " + r.getTimeSlotId()
                    + " — Parent ID: " + r.getParentId()
                    + " — Date: " + r.getRegistrationDate();
                if (r.getPayment() != null) {
                    line += " | Payé: " + String.format("%.2f", r.getPayment().getPaidAmount())
                          + " / " + String.format("%.2f", r.getPayment().getTotalAmount()) + " €";
                }
                lines.add(line);
            }
            registrationsListView.setItems(FXCollections.observableArrayList(lines));
        }
    }

    /** Affiche tous les créneaux et leur disponibilité. */
    @FXML
    public void showTimeSlots() {
        if (timeSlotsListView == null) return;
        List<TimeSlot> slots = timeSlotService.getAllTimeSlots();
        if (slots.isEmpty()) {
            timeSlotsListView.setItems(FXCollections.observableArrayList("Aucun créneau."));
        } else {
            java.util.List<String> lines = new java.util.ArrayList<>();
            for (TimeSlot ts : slots) {
                String dispo = ts.isAvailable()
                    ? "✓ Disponible (" + ts.getRegistrationCount() + "/" + ts.getMaxCapacity() + ")"
                    : "✗ Complet";
                String line = "📅 " + ts.getSubject()
                    + " — " + ts.getSchedule().toLocalDate()
                    + " à " + ts.getSchedule().toLocalTime()
                    + " — " + ts.getClassroom().getName()
                    + " — " + dispo;
                lines.add(line);
            }
            timeSlotsListView.setItems(FXCollections.observableArrayList(lines));
        }
    }

    /** Affiche tous les enfants inscrits. */
    @FXML
    public void showChildren() {
        if (childrenListView == null) return;
        List<Child> children = childRepository.findAll();
        if (children.isEmpty()) {
            childrenListView.setItems(FXCollections.observableArrayList("Aucun enfant enregistré."));
        } else {
            java.util.List<String> lines = new java.util.ArrayList<>();
            for (Child c : children) {
                String line = "👦 " + c.getFirstName() + " " + c.getLastName();
                if (c.getLevel() != null && !c.getLevel().isEmpty()) line += " — " + c.getLevel();
                if (c.getAge() > 0) line += " — " + c.getAge() + " ans";
                line += " (Parent ID: " + c.getParentId() + ")";
                lines.add(line);
            }
            childrenListView.setItems(FXCollections.observableArrayList(lines));
        }
    }

    /** Affiche les notifications envoyées. */
    @FXML
    public void showNotifications() {
        if (notificationsListView == null) return;
        var notifs = notificationRepository.findAll();
        if (notifs.isEmpty()) {
            notificationsListView.setItems(FXCollections.observableArrayList("Aucune notification."));
        } else {
            java.util.List<String> lines = new java.util.ArrayList<>();
            for (var n : notifs) {
                String status = n.isSent() ? "✓ Envoyée" : "⏳ En attente";
                String date = n.getSentDate() != null ? " le " + n.getSentDate().toLocalDate() : "";
                lines.add("[" + status + date + "] Parent " + n.getParentId() + " : " + n.getMessage());
            }
            notificationsListView.setItems(FXCollections.observableArrayList(lines));
        }
    }

    /** Met à jour le label de statistiques. */
    private void updateStats() {
        if (statsLabel == null) return;
        int nbInscriptions = registrationService.getAllRegistrations().size();
        int nbCreneaux = timeSlotService.getAllTimeSlots().size();
        int nbEnfants = childRepository.findAll().size();
        statsLabel.setText(nbInscriptions + " inscription(s)  |  "
            + nbCreneaux + " créneau(x)  |  "
            + nbEnfants + " enfant(s)");
    }

    @FXML
    public void logout() {
        try {
            Stage stage = (Stage) adminLabel.getScene().getWindow();
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
