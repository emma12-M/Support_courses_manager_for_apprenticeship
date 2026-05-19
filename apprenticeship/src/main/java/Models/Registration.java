package Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDate;

/**
 * Représente l'inscription d'un enfant à un créneau, avec un paiement associé.
 *
 * CORRECTION BUG CRITIQUE — Récursion infinie :
 *   Avant : Registration contenait TimeSlot, qui contenait List<Registration>
 *   → Jackson entrait dans une boucle infinie à la sérialisation.
 *
 *   SOLUTION : on stocke les IDs (childId, timeSlotId) plutôt que les objets complets.
 *   Le Payment est stocké directement car il ne crée pas de cycle.
 *   Les objets Child et TimeSlot complets sont gardés @JsonIgnore en mémoire
 *   pour être utilisés par l'application, mais ne sont pas écrits dans le JSON.
 *
 * FORMAT JSON de registration.json :
 * {
 *   "id": 1,
 *   "childId": 14426,
 *   "timeSlotId": 5,
 *   "parentId": 2,
 *   "registrationDate": "2026-05-18",
 *   "payment": { ... }
 * }
 */
public class Registration {

    private int id;

    // On stocke uniquement les IDs pour éviter la récursion
    private int childId;
    private int timeSlotId;
    private int parentId;

    private LocalDate registrationDate;

    // Le Payment est stocké directement (il ne crée pas de cycle)
    private Payment payment;

    // Objets complets — uniquement en mémoire, pas sérialisés
    @JsonIgnore
    private transient Child child;
    @JsonIgnore
    private transient TimeSlot timeSlot;

    // Constructeur vide — OBLIGATOIRE pour Jackson
    public Registration() {
    }

    /**
     * Constructeur principal utilisé par RegistrationService.
     */
    public Registration(int id, int childId, int timeSlotId, int parentId,
                        LocalDate registrationDate, Payment payment) {
        this.id = id;
        this.childId = childId;
        this.timeSlotId = timeSlotId;
        this.parentId = parentId;
        this.registrationDate = registrationDate;
        this.payment = payment;
    }

    // GETTERS
    public int getId() { return id; }
    public int getChildId() { return childId; }
    public int getTimeSlotId() { return timeSlotId; }
    public int getParentId() { return parentId; }
    public LocalDate getRegistrationDate() { return registrationDate; }
    public Payment getPayment() { return payment; }

    // Objets en mémoire uniquement
    public Child getChild() { return child; }
    public TimeSlot getTimeSlot() { return timeSlot; }

    // SETTERS
    public void setId(int id) { this.id = id; }
    public void setChildId(int childId) { this.childId = childId; }
    public void setTimeSlotId(int timeSlotId) { this.timeSlotId = timeSlotId; }
    public void setParentId(int parentId) { this.parentId = parentId; }
    public void setRegistrationDate(LocalDate registrationDate) { this.registrationDate = registrationDate; }
    public void setPayment(Payment payment) { this.payment = payment; }
    public void setChild(Child child) { this.child = child; }
    public void setTimeSlot(TimeSlot timeSlot) { this.timeSlot = timeSlot; }
}


