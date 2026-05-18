package Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import exceptions.CapacityException;
import interfaces.ITimeSlotState;
import states.AvailableState;
import states.FullState;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Représente un créneau de cours de soutien.
 *
 * CORRECTION BUG CRITIQUE — Récursion infinie :
 *   TimeSlot contenait List<Registration>, et Registration contenait TimeSlot.
 *   Jackson entrait dans une boucle infinie à la sérialisation.
 *   SOLUTION : @JsonIgnore sur registrations.
 *   On stocke le nombre d'inscrits (registrationCount) à la place,
 *   qui est recalculé au chargement depuis registration.json.
 *
 * PATTERN STATE :
 *   Le TimeSlot délègue l'action "s'inscrire" à son état courant.
 *   - AvailableState : accepte l'inscription
 *   - FullState      : refuse et lance CapacityException
 */
public class TimeSlot {

    private int id;
    private String subject;
    private LocalDateTime schedule;
    private ClassRoom classroom;
    private int maxCapacity;

    // Nombre d'inscrits — persisté en JSON pour recalculer l'état au démarrage
    private int registrationCount;

    // CORRECTION : @JsonIgnore pour briser la récursion infinie
    // La liste complète des inscriptions n'est PAS stockée dans timeSlots.json
    // Elle est gérée en mémoire et reconstruite depuis registration.json
    @JsonIgnore
    private List<Registration> registrations;

    // L'état courant n'est pas sérialisé — il est recalculé via refreshState()
    @JsonIgnore
    private ITimeSlotState state;

    // Constructeur vide — OBLIGATOIRE pour Jackson
    public TimeSlot() {
        registrations = new ArrayList<>();
        state = new AvailableState();
        registrationCount = 0;
    }

    public TimeSlot(int id, String subject, LocalDateTime schedule,
                    ClassRoom classroom, int maxCapacity) {
        this.id = id;
        this.subject = subject;
        this.schedule = schedule;
        this.classroom = classroom;
        this.maxCapacity = maxCapacity;
        registrations = new ArrayList<>();
        state = new AvailableState();
        registrationCount = 0;
    }

    /**
     * Tente d'inscrire un élève.
     * Délègue au state courant (PATTERN STATE).
     * Lance CapacityException si le créneau est complet.
     */
    public void register() throws CapacityException {
        state.register(this);
    }

    /**
     * Met à jour l'état et le compteur après une inscription.
     * Appelé par RegistrationService après chaque inscription réussie.
     */
    public void incrementRegistrationCount() {
        registrationCount++;
        refreshState();
    }

    /**
     * Recalcule l'état selon registrationCount.
     * Appelé au démarrage pour restaurer l'état correct depuis le JSON.
     */
    public void refreshState() {
        if (registrationCount >= maxCapacity) {
            state = new FullState();
        } else {
            state = new AvailableState();
        }
    }

    /**
     * Retourne true si le créneau accepte encore des inscriptions.
     */
    @JsonIgnore
    public boolean isAvailable() {
        return registrationCount < maxCapacity;
    }

    /**
     * Affichage dans les ComboBox JavaFX.
     */
    @Override
    public String toString() {
        String dispo = isAvailable()
            ? "✓ Disponible (" + registrationCount + "/" + maxCapacity + ")"
            : "✗ Complet";
        return subject + " — " + schedule.toLocalDate()
               + " à " + schedule.toLocalTime()
               + " [" + dispo + "]";
    }

    // --- GETTERS ---
    public int getId() { return id; }
    public String getSubject() { return subject; }
    public LocalDateTime getSchedule() { return schedule; }
    public ClassRoom getClassroom() { return classroom; }
    public int getMaxCapacity() { return maxCapacity; }
    public int getRegistrationCount() { return registrationCount; }

    @JsonIgnore
    public List<Registration> getRegistrations() { return registrations; }

    @JsonIgnore
    public ITimeSlotState getState() { return state; }

    // --- SETTERS ---
    public void setId(int id) { this.id = id; }
    public void setSubject(String subject) { this.subject = subject; }
    public void setSchedule(LocalDateTime schedule) { this.schedule = schedule; }
    public void setClassroom(ClassRoom classroom) { this.classroom = classroom; }
    public void setMaxCapacity(int maxCapacity) { this.maxCapacity = maxCapacity; }
    public void setRegistrationCount(int registrationCount) {
        this.registrationCount = registrationCount;
        refreshState(); // recalcule l'état automatiquement
    }
    public void setRegistrations(List<Registration> registrations) {
        this.registrations = registrations;
    }

    @JsonIgnore
    public void setState(ITimeSlotState state) { this.state = state; }
}


