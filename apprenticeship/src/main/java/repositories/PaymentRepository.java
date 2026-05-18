package repositories;

import Models.Payment;
import com.fasterxml.jackson.core.type.TypeReference;
import singleton.AppConfig;

import java.util.List;

public class PaymentRepository extends BaseRepository<Payment> {

    public PaymentRepository() {
        super(
            AppConfig.getInstance().getPaymentsFilePath(),
            new TypeReference<List<Payment>>() {}
        );
    }

    /**
     * Cherche un paiement par son ID.
     */
    public Payment findById(int id) {
        for (Payment p : items) {
            if (p.getId() == id) return p;
        }
        return null;
    }

    /**
     * CORRECTION : met à jour un paiement existant par son ID.
     * Remplace l'ancien objet et sauvegarde dans payment.json.
     */
    @Override
    public void update(Payment payment) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getId() == payment.getId()) {
                items.set(i, payment);
                write();
                return;
            }
        }
        // Si le paiement n'existe pas encore, on l'ajoute
        save(payment);
    }
}
