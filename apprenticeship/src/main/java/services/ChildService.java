package services;

import Models.Child;
import Models.Parent;
import repositories.ChildRepository;
import repositories.ParentRepository;

import java.util.List;

/**
 * Service pour gérer les enfants.
 *
 * RÈGLE IMPORTANTE :
 *   Quand on crée un enfant, il faut TOUJOURS :
 *   1. Définir child.parentId = parent.getId()
 *   2. Sauvegarder l'enfant dans children.json
 *   3. Ajouter l'enfant à parent.children (liste en mémoire)
 *   4. Sauvegarder le parent mis à jour dans parents.json
 *
 *   Ces 4 étapes garantissent que les deux fichiers JSON sont cohérents.
 */
public class ChildService {

    private ChildRepository childRepository;
    private ParentRepository parentRepository;

    public ChildService() {
        childRepository = new ChildRepository();
        parentRepository = new ParentRepository();
    }

    /**
     * Crée un enfant et le lie à son parent dans les deux fichiers JSON.
     *
     * @param parent Le parent auquel rattacher l'enfant
     * @param child  L'enfant à créer (sans ID ni parentId encore)
     * @return L'enfant créé avec son ID
     */
    public Child addChildToParent(Parent parent, Child child) {

        // 1. Génère un ID unique si l'enfant n'en a pas encore
        if (child.getId() <= 0) {
            child.setId(generateChildId());
        }

        // 2. IMPORTANT : définit le parentId sur l'enfant
        //    C'est ce qui permet de retrouver les enfants d'un parent
        child.setParentId(parent.getId());

        // 3. Sauvegarde l'enfant dans children.json
        childRepository.save(child);

        // 4. Ajoute l'enfant à la liste en mémoire du parent
        parent.addChild(child);

        // 5. Sauvegarde le parent mis à jour dans parents.json
        parentRepository.update(parent);

        return child;
    }

    /**
     * Retourne tous les enfants d'un parent donné.
     * Utilise le champ parentId dans children.json.
     */
    public List<Child> getChildrenByParentId(int parentId) {
        return childRepository.findByParentId(parentId);
    }

    /**
     * Génère un ID unique pour un enfant.
     * Utilise le timestamp pour éviter les doublons.
     */
    private int generateChildId() {
        return (int) (System.currentTimeMillis() % 100000);
    }
}