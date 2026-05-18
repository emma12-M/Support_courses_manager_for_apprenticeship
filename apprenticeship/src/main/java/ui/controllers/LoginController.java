package ui.controllers;

import Models.Administrator;
import Models.Parent;
import Models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import managers.SessionManager;
import services.AuthService;

public class LoginController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    private AuthService authService;

    public LoginController() {
        authService = new AuthService();
    }

    @FXML
    public void handleLogin(ActionEvent event) {

        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();

        // Vérifie que les champs ne sont pas vides
        if (email.isEmpty() || password.isEmpty()) {
            showError("Veuillez remplir tous les champs.");
            return;
        }

        // Tente la connexion — retourne null si échec
        User user = authService.login(email, password);

        if (user == null) {
            showError("Email ou mot de passe incorrect.");
            return;
        }

        // Connexion réussie — enregistre dans SessionManager
        SessionManager.getInstance().setCurrentUser(user);

        // On ouvre le bon dashboard selon le rôle
        try {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            if (user instanceof Administrator) {
                // C'est un administrateur
                openDashboard(stage, "/ui/views/dashboard_administrator.fxml", user);
            } else if (user instanceof Parent) {
                // C'est un parent
                openDashboard(stage, "/ui/views/dashboard_parent.fxml", user);
            }

        } catch (Exception e) {
            e.printStackTrace();
            showError("Erreur lors de l'ouverture du dashboard.");
        }
    }

    /**
     * Ouvre un dashboard et transmet l'utilisateur connecté au contrôleur.
     */
    private void openDashboard(Stage stage, String fxmlPath, User user) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Scene scene = new Scene(loader.load(), 800, 600);

        // On récupère le contrôleur du dashboard et on lui passe l'utilisateur
        Object controller = loader.getController();

        if (controller instanceof DashboardParentController) {
            ((DashboardParentController) controller).setCurrentUser((Parent) user);
        } else if (controller instanceof DashboardAdministratorController) {
            ((DashboardAdministratorController) controller).setCurrentUser((Administrator) user);
        }

        stage.setScene(scene);
        stage.setTitle("Support Course Management");
        stage.show();
    }

    /**
     * Affiche une alerte d'erreur.
     */
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur de connexion");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }
}

