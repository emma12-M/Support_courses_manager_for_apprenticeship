package repositories;

import Models.TimeSlot;
import com.fasterxml.jackson.core.type.TypeReference;
import singleton.AppConfig;

import java.util.List;

public class TimeSlotRepository extends BaseRepository<TimeSlot> {

    public TimeSlotRepository() {
        super(
            AppConfig.getInstance().getTimeSlotsFilePath(),
            new TypeReference<List<TimeSlot>>() {}
        );
        // Recalcule les états au chargement depuis le JSON
        // (AvailableState ou FullState selon registrationCount vs maxCapacity)
        for (TimeSlot ts : items) {
            ts.refreshState();
        }
    }

    /**
     * Cherche un créneau par son ID.
     */
    public TimeSlot findById(int id) {
        for (TimeSlot ts : items) {
            if (ts.getId() == id) return ts;
        }
        return null;
    }

    /**
     * CORRECTION : met à jour un créneau existant dans la liste par son ID.
     * Remplace l'ancien objet par le nouveau et sauvegarde.
     */
    @Override
    public void update(TimeSlot timeSlot) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getId() == timeSlot.getId()) {
                items.set(i, timeSlot);
                break;
            }
        }
        write();
    }
}
