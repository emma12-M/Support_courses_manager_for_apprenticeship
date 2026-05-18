package managers;

import Models.User;

/**
 * Gestionnaire de session global de l'application.
 * Permet de savoir quel utilisateur est actuellement connecté.
 * 
 * Utilise le pattern Singleton.
 */
public class SessionManager {

    private static SessionManager instance;

    private User currentUser;

    private SessionManager() {
        currentUser = null;
    }

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    /**
     * Enregistre l'utilisateur connecté
     */
    public void setCurrentUser(User user) {
        currentUser = user;
    }

    /**
     * Retourne l'utilisateur actuellement connecté
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * Retourne l'ID de l'utilisateur connecté
     */
    public int getCurrentUserId() {
        return currentUser != null ? currentUser.getId() : -1;
    }

    /**
     * Vérifie si un utilisateur est connecté
     */
    public boolean isLoggedIn() {
        return currentUser != null;
    }

    /**
     * Déconnecte l'utilisateur (logout)
     */
    public void logout() {
        currentUser = null;
    }
}
