package repositories;

import Models.Child;
import com.fasterxml.jackson.core.type.TypeReference;
import singleton.AppConfig;

import java.util.List;
import java.util.stream.Collectors;

public class ChildRepository extends BaseRepository<Child> {

    public ChildRepository() {
        super(
            AppConfig.getInstance().getChildrenFilePath(),
            new TypeReference<List<Child>>() {}
        );
    }

    /**
     * Retourne tous les enfants appartenant à un parent donné.
     * Utilise le champ parentId ajouté dans Child.
     */
    public List<Child> findByParentId(int parentId) {
        return items.stream()
            .filter(c -> c.getParentId() == parentId)
            .collect(Collectors.toList());
    }
}
