package Models;

import java.time.LocalDateTime;

/**
 * Représente une notification envoyée à un parent.
 *
 * POURQUOI ces champs ?
 *  - parentId : pour savoir à QUEL parent la notification appartient
 *  - sent     : pour tracer si le rappel a bien été envoyé (true/false)
 *  - sentDate : la date et heure d'envoi réel
 *
 * Ces trois champs sont persistés en JSON dans notifications.json.
 * On peut ainsi vérifier : "Est-ce que le rappel du 15 mai a bien été envoyé ?"
 */
public class Notification {

    private int id;

    // ID du parent concerné — permet de filtrer par parent
    private int parentId;

    private String message;

    // true = notification envoyée, false = en attente
    private boolean sent;

    // Date à laquelle la notification a été envoyée (null si pas encore envoyée)
    private LocalDateTime sentDate;

    // Constructeur vide — OBLIGATOIRE pour Jackson
    public Notification() {
        this.sent = false;
    }

    /**
     * Crée une notification non encore envoyée.
     * sent = false, sentDate = null
     */
    public Notification(int id, int parentId, String message) {
        this.id = id;
        this.parentId = parentId;
        this.message = message;
        this.sent = false;
        this.sentDate = null;
    }

    /**
     * Marque la notification comme envoyée.
     * Appelé par ParentObserver.update()
     */
    public void markAsSent() {
        this.sent = true;
        this.sentDate = LocalDateTime.now();
    }

    // GETTERS
    public int getId() { return id; }
    public int getParentId() { return parentId; }
    public String getMessage() { return message; }
    public boolean isSent() { return sent; }
    public LocalDateTime getSentDate() { return sentDate; }

    // SETTERS — nécessaires pour Jackson (désérialisation)
    public void setId(int id) { this.id = id; }
    public void setParentId(int parentId) { this.parentId = parentId; }
    public void setMessage(String message) { this.message = message; }
    public void setSent(boolean sent) { this.sent = sent; }
    public void setSentDate(LocalDateTime sentDate) { this.sentDate = sentDate; }
}


