package facades;

import Models.Child;
import Models.Parent;
import Models.Payment;
import Models.Registration;
import Models.TimeSlot;
import exceptions.CapacityException;
import services.NotificationService;
import services.PaymentService;
import services.RegistrationService;

/**
 * PATTERN FACADE — Interface simplifiée pour le processus d'inscription.
 *
 * POURQUOI une Facade ?
 *   Sans elle, le controller JavaFX devrait appeler 4 services dans le bon ordre.
 *   Avec elle, le controller fait UN SEUL appel : completeRegistration(...)
 *   La Facade orchestre tout en interne.
 *
 * CE QUE FAIT completeRegistration :
 *   Étape 1 : Inscrit l'enfant au créneau (RegistrationService → PATTERN STATE)
 *   Étape 2 : Traite le premier paiement (PaymentService → PATTERN STRATEGY)
 *   Étape 3 : Notifie le parent (NotificationService → PATTERN OBSERVER)
 *
 * CORRECTIONS apportées :
 *   - parentId est maintenant transmis à RegistrationService
 *   - subscribeParent() passe l'ID du parent à ParentObserver
 *     (pour persister la notification correctement)
 *   - clearObservers() évite les doublons si plusieurs inscriptions
 */
public class ManagementFacade {

    private RegistrationService registrationService;
    private PaymentService paymentService;
    private NotificationService notificationService;

    public ManagementFacade() {
        registrationService = new RegistrationService();
        paymentService = new PaymentService();
        notificationService = new NotificationService();
    }

    /**
     * Effectue le processus complet d'inscription.
     *
     * @param parent    Le parent connecté
     * @param child     L'enfant à inscrire
     * @param timeSlot  Le créneau choisi
     * @param payment   Le paiement (montant, type, versements)
     * @throws CapacityException si le créneau est complet
     */
   public void completeRegistration(Parent parent, Child child,
                                 TimeSlot timeSlot, Payment payment)
        throws CapacityException {

    // ÉTAPE 1 — Inscription
    Registration registration = registrationService.registerChild(
        child, timeSlot, payment, parent.getId()
    );

    // ÉTAPE 2 — Traitement du paiement
    double paidAmount = paymentService.processPayment(payment);

    // ÉTAPE 3 — Notification ciblée au parent (SOLUTION B)
    String msg = "Inscription de " + child.getFirstName() + " " + child.getLastName()
        + " au créneau \"" + timeSlot.getSubject() + "\""
        + " confirmée. Montant du premier versement : "
        + String.format("%.2f", payment.getPaidAmount()) + " €";

    notificationService.notifyParent(parent.getId(), msg);  // ✅ Ciblé

    System.out.println("[ManagementFacade] Inscription complete effectuee.");
}
}
