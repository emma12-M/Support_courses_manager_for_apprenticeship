package repositories;

import Models.Notification;
import com.fasterxml.jackson.core.type.TypeReference;
import singleton.AppConfig;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Repository pour les notifications.
 * Persiste dans notifications.json.
 *
 * POURQUOI ce repository existe-t-il ?
 * Le projet demande de pouvoir "savoir si une notification a été envoyée".
 * Sans persistance, les notifications disparaissent au redémarrage.
 * Ce repository sauvegarde chaque notification avec son champ sent=true/false.
 */
public class NotificationRepository extends BaseRepository<Notification> {

    public NotificationRepository() {
        super(
            AppConfig.getInstance().getNotificationsFilePath(),
            new TypeReference<List<Notification>>() {}
        );
    }

    /**
     * Retourne toutes les notifications d'un parent donné.
     */
    public List<Notification> findByParentId(int parentId) {
        return items.stream()
            .filter(n -> n.getParentId() == parentId)
            .collect(Collectors.toList());
    }

    /**
     * Retourne les notifications non encore envoyées (sent = false).
     */
    public List<Notification> findPending() {
        return items.stream()
            .filter(n -> !n.isSent())
            .collect(Collectors.toList());
    }

    /**
     * Génère un ID unique pour une nouvelle notification.
     */
    public int generateNextId() {
        int maxId = 0;
        for (Notification n : items) {
            if (n.getId() > maxId) maxId = n.getId();
        }
        return maxId + 1;
    }
}