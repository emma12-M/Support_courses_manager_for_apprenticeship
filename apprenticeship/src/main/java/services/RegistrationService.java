package services;

import Models.Child;
import Models.Payment;
import Models.Registration;
import Models.TimeSlot;
import exceptions.CapacityException;
import repositories.PaymentRepository;
import repositories.RegistrationRepository;
import repositories.TimeSlotRepository;

import java.time.LocalDate;
import java.util.List;

/**
 * Service d'inscription d'un enfant à un créneau.
 *
 * CORRECTIONS apportées :
 * 1. Registration stocke maintenant des IDs (pas des objets complets)
 *    → plus de récursion infinie dans le JSON
 * 2. Le paiement est sauvegardé dans payment.json via PaymentRepository
 *    → avant : le paiement disparaissait à chaque redémarrage
 * 3. Le TimeSlot est mis à jour (registrationCount++) et sauvegardé
 *    → timeSlots.json reflète bien le nombre d'inscrits
 */
public class RegistrationService {

    private RegistrationRepository registrationRepository;
    private PaymentRepository paymentRepository;
    private TimeSlotRepository timeSlotRepository;

    public RegistrationService() {
        registrationRepository = new RegistrationRepository();
        paymentRepository = new PaymentRepository();
        timeSlotRepository = new TimeSlotRepository();
    }

    /**
     * Inscrit un enfant à un créneau avec un paiement.
     *
     * @param child     L'enfant à inscrire
     * @param timeSlot  Le créneau choisi
     * @param payment   Le paiement associé
     * @param parentId  L'ID du parent (pour lier l'inscription)
     * @return L'objet Registration créé et sauvegardé
     * @throws CapacityException si le créneau est complet
     */
    public Registration registerChild(Child child, TimeSlot timeSlot,
                                      Payment payment, int parentId)
            throws CapacityException {

        // 1. Vérifie que le créneau accepte encore des inscriptions
        //    Lance CapacityException si FullState (PATTERN STATE)
        timeSlot.register();

        // 2. Calcule un nouvel ID unique pour l'inscription
        int newRegId = generateNextRegistrationId();

        // 3. Crée l'inscription avec des IDs (pas d'objets imbriqués)
        Registration registration = new Registration(
            newRegId,
            child.getId(),
            timeSlot.getId(),
            parentId,
            LocalDate.now(),
            payment
        );

        // 4. Sauvegarde l'inscription dans registration.json
        registrationRepository.save(registration);

        // 5. Sauvegarde le paiement dans payment.json
        //    CORRECTION : avant, payment.json restait vide
        paymentRepository.save(payment);

        // 6. Met à jour le compteur du créneau et recalcule son état
        timeSlot.incrementRegistrationCount();
        timeSlotRepository.update(timeSlot);

        return registration;
    }

    /**
     * Retourne toutes les inscriptions.
     */
    public List<Registration> getAllRegistrations() {
        return registrationRepository.findAll();
    }

    /**
     * Retourne les inscriptions d'un parent donné.
     */
    public List<Registration> getRegistrationsByParent(int parentId) {
        return registrationRepository.findByParentId(parentId);
    }

    /**
     * Génère un ID unique pour une nouvelle inscription.
     */
    private int generateNextRegistrationId() {
        List<Registration> all = registrationRepository.findAll();
        int maxId = 0;
        for (Registration r : all) {
            if (r.getId() > maxId) maxId = r.getId();
        }
        return maxId + 1;
    }
}

