package observers;

import Models.Notification;
import interfaces.Observer;
import repositories.NotificationRepository;

/**
 * PATTERN OBSERVER — Observateur côté parent.
 *
 * Quand NotificationService appelle notifyObservers(message),
 * chaque ParentObserver reçoit le message via update().
 *
 * AVANT (problème) : update() imprimait juste en console.
 *   → Aucune trace dans le système, notification non persistée.
 *
 * APRÈS (correction) : update() :
 *   1. Crée un objet Notification avec parentId et message
 *   2. Marque sent = true (la notification est bien "envoyée")
 *   3. Sauvegarde dans notifications.json via NotificationRepository
 *
 * Cela répond à l'exigence : "savoir si un rappel a bien été envoyé".
 */
public class ParentObserver implements Observer {

    private int parentId;
    private String parentName;
    private NotificationRepository notificationRepository;

    public ParentObserver(int parentId, String parentName) {
        this.parentId = parentId;
        this.parentName = parentName;
        this.notificationRepository = new NotificationRepository();
    }

    @Override
    public void update(String message) {
        // 1. Génère un ID unique pour cette notification
        int newId = notificationRepository.generateNextId();

        // 2. Crée la notification
        Notification notification = new Notification(newId, parentId, message);

        // 3. Marque comme envoyée (sent = true, sentDate = maintenant)
        notification.markAsSent();

        // 4. Sauvegarde dans notifications.json
        notificationRepository.save(notification);

        // Log console pour debug
        System.out.println("Notification envoyée à " + parentName
            + " [ID=" + newId + "] : " + message);
    }
}