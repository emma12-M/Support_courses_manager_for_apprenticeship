package states;

import exceptions.CapacityException;
import interfaces.ITimeSlotState;
import Models.TimeSlot;

/**
 * PATTERN STATE — État "disponible" d'un créneau.
 *
 * CORRECTION BUG CRITIQUE :
 *   Avant : on comptait registrations.size() (liste en mémoire, toujours vide
 *           car @JsonIgnore, donc le créneau n'était JAMAIS marqué comme complet)
 *
 *   Après : on utilise registrationCount qui est le vrai compteur persisté
 *           en JSON et incrémenté par RegistrationService.
 *
 * FONCTIONNEMENT :
 *   register() est appelé depuis TimeSlot.register() (via la Facade).
 *   Si le créneau est disponible → on accepte l'inscription.
 *   Si après cette inscription il est plein → on passe à FullState.
 */
public class AvailableState implements ITimeSlotState {

    @Override
    public void register(TimeSlot timeSlot) throws CapacityException {

        // CORRECTION : on compare registrationCount (vrai compteur)
        // et non plus registrations.size() (liste vide en mémoire)
        if (timeSlot.getRegistrationCount() >= timeSlot.getMaxCapacity()) {

            // Le créneau est déjà complet — on change l'état et on refuse
            timeSlot.setState(new FullState());
            throw new CapacityException(
                "Le créneau est complet (max " + timeSlot.getMaxCapacity() + " inscrits)."
            );
        }

        // Inscription acceptée — l'incrémentation se fait dans RegistrationService
        System.out.println("[AvailableState] Inscription acceptée pour : "
            + timeSlot.getSubject()
            + " (" + timeSlot.getRegistrationCount() + "/" + timeSlot.getMaxCapacity() + ")");
    }
}
