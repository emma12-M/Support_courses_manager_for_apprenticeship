package singleton;

/**
 * PATTERN SINGLETON — Configuration centrale de l'application.
 *
 * POURQUOI un Singleton ?
 *   On veut UN SEUL objet de configuration partagé dans toute l'application.
 *   Si on créait un new AppConfig() à chaque endroit, on risquerait des
 *   valeurs différentes selon l'instance. Le Singleton garantit l'unicité.
 *
 * COMMENT ça marche ?
 *   - Le constructeur est PRIVÉ → impossible de faire new AppConfig()
 *   - AppConfig.getInstance() retourne toujours la même instance
 *
 * UTILISATION :
 *   int max = AppConfig.getInstance().getMaxInstallments();
 */
public class AppConfig {

    // L'unique instance de cette classe
    private static AppConfig instance;

    // ---- Paramètres de l'application ----

    private final String applicationName = "Gestion des Cours de Soutien";

    // Nombre maximum de versements autorisés (sujet : max 6)
    private final int maxInstallments = 6;

    // Montant minimum d'un paiement
    private final double minPaymentAmount = 1.0;

    // Chemins des fichiers JSON (relatifs à la racine du projet Maven)
    private final String parentsFilePath      = "src/main/resources/data/parents.json";
    private final String childrenFilePath     = "src/main/resources/data/children.json";
    private final String timeSlotsFilePath    = "src/main/resources/data/timeSlots.json";
    private final String registrationsFilePath= "src/main/resources/data/registration.json";
    private final String paymentsFilePath     = "src/main/resources/data/payment.json";
    private final String notificationsFilePath= "src/main/resources/data/notifications.json";
    private final String adminFilePath        = "src/main/resources/data/administrator.json";

    // Constructeur PRIVÉ — personne ne peut faire new AppConfig()
    private AppConfig() {}

    /**
     * Retourne l'unique instance d'AppConfig.
     * La crée si elle n'existe pas encore.
     */
    public static AppConfig getInstance() {
        if (instance == null) {
            instance = new AppConfig();
        }
        return instance;
    }

    // GETTERS — en lecture seule (pas de setters pour les constantes)
    public String getApplicationName()       { return applicationName; }
    public int    getMaxInstallments()       { return maxInstallments; }
    public double getMinPaymentAmount()      { return minPaymentAmount; }
    public String getParentsFilePath()       { return parentsFilePath; }
    public String getChildrenFilePath()      { return childrenFilePath; }
    public String getTimeSlotsFilePath()     { return timeSlotsFilePath; }
    public String getRegistrationsFilePath() { return registrationsFilePath; }
    public String getPaymentsFilePath()      { return paymentsFilePath; }
    public String getNotificationsFilePath() { return notificationsFilePath; }
    public String getAdminFilePath()         { return adminFilePath; }
}

