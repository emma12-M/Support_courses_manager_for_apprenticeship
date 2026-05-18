package services;

import Models.Administrator;
import Models.Parent;
import Models.User;
import repositories.AdministratorRepository;
import repositories.ParentRepository;

public class AuthService {

    private ParentRepository parentRepository;
    private AdministratorRepository administratorRepository;

    public AuthService() {
        parentRepository = new ParentRepository();
        administratorRepository = new AdministratorRepository();
    }

    /**
     * Tente de connecter un utilisateur.
     * Cherche d'abord parmi les parents, puis parmi les administrateurs.
     * 
     * @return l'objet User (Parent ou Administrator) si trouvé, null sinon
     */
    public User login(String email, String password) {

        // 1. Cherche dans les parents
        Parent parent = parentRepository.findByEmail(email);
        if (parent != null && parent.getPassword().equals(password)) {
            return parent; // connexion réussie en tant que parent
        }

        // 2. Cherche dans les administrateurs
        for (Administrator admin : administratorRepository.findAll()) {
            if (admin.getEmail().equals(email)
                    && admin.getPassword().equals(password)) {
                return admin; // connexion réussie en tant qu'admin
            }
        }

        // 3. Aucun utilisateur trouvé
        return null;
    }
}

