package repositories;

import Models.Registration;
import com.fasterxml.jackson.core.type.TypeReference;
import singleton.AppConfig;

import java.util.List;
import java.util.stream.Collectors;

public class RegistrationRepository extends BaseRepository<Registration> {

    public RegistrationRepository() {
        super(
            AppConfig.getInstance().getRegistrationsFilePath(),
            new TypeReference<List<Registration>>() {}
        );
    }

    /**
     * Retourne toutes les inscriptions d'un parent donné.
     */
    public List<Registration> findByParentId(int parentId) {
        return items.stream()
            .filter(r -> r.getParentId() == parentId)
            .collect(Collectors.toList());
    }

    /**
     * Retourne toutes les inscriptions d'un enfant donné.
     */
    public List<Registration> findByChildId(int childId) {
        return items.stream()
            .filter(r -> r.getChildId() == childId)
            .collect(Collectors.toList());
    }
}
