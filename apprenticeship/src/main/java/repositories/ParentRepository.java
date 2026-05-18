package repositories;

import Models.Parent;
import com.fasterxml.jackson.core.type.TypeReference;
import singleton.AppConfig;

import java.util.List;

public class ParentRepository extends BaseRepository<Parent> {

    public ParentRepository() {
        super(
            AppConfig.getInstance().getParentsFilePath(),
            new TypeReference<List<Parent>>() {}
        );
    }

    /**
     * Cherche un parent par son email.
     * Utilisé par AuthService pour l'authentification.
     */
    public Parent findByEmail(String email) {
        for (Parent parent : items) {
            if (parent.getEmail() != null && parent.getEmail().equals(email)) {
                return parent;
            }
        }
        return null;
    }

    /**
     * Cherche un parent par son ID.
     */
    public Parent findById(int id) {
        for (Parent parent : items) {
            if (parent.getId() == id) {
                return parent;
            }
        }
        return null;
    }

    /**
     * CORRECTION : met à jour un parent existant dans la liste par son ID.
     * Avant : BaseRepository.update() écrivait juste sans remplacer.
     * Après : on cherche le parent par ID et on le remplace.
     */
    @Override
    public void update(Parent parent) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getId() == parent.getId()) {
                items.set(i, parent);
                break;
            }
        }
        write();
    }
}
