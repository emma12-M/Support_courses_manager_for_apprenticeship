package repositories;

import Models.Administrator;
import com.fasterxml.jackson.core.type.TypeReference;
import singleton.AppConfig;

import java.util.List;

/**
 * Repository pour les administrateurs.
 * CORRECTION : utilise AppConfig.getAdminFilePath() au lieu d'un chemin en dur.
 */
public class AdministratorRepository extends BaseRepository<Administrator> {

    public AdministratorRepository() {
        super(
            AppConfig.getInstance().getAdminFilePath(),
            new TypeReference<List<Administrator>>() {}
        );
    }

    /** Cherche un admin par son email. */
    public Administrator findByEmail(String email) {
        for (Administrator admin : items) {
            if (admin.getEmail() != null && admin.getEmail().equals(email)) {
                return admin;
            }
        }
        return null;
    }
}
