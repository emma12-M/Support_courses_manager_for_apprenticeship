package utils;

import Models.Child;
import Models.Parent;
import Models.Registration;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import repositories.ChildRepository;
import repositories.RegistrationRepository;
import repositories.TimeSlotRepository;
import singleton.AppConfig;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * ✅ NOUVEL UTILITAIRE - Nettoyer les données incohérentes
 *
 * Utilisation:
 *   DataCleanupUtil.cleanRegistrationData();
 *   DataCleanupUtil.cleanChildrenData();
 *   DataCleanupUtil.reportInconsistencies();
 *
 * Résout les problèmes:
 *   1. Les inscriptions avec childId=0, timeSlotId=0
 *   2. Les enfants avec parentId=0
 *   3. Les enfants orphelins (pas d'inscription)
 */
public class DataCleanupUtil {

    private static ObjectMapper mapper;

    static {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * ✅ Nettoie registration.json - supprime les entrées avec childId=0 ou timeSlotId=0
     */
    public static void cleanRegistrationData() {
        try {
            String registrationPath = AppConfig.getInstance().getRegistrationsFilePath();
            File file = new File(registrationPath);

            if (!file.exists()) {
                System.out.println("[DataCleanupUtil] registration.json n'existe pas.");
                return;
            }

            // Charger les données actuelles
            List<Registration> registrations = mapper.readValue(
                file,
                new TypeReference<List<Registration>>() {}
            );

            // Compter les entrées avant
            int countBefore = registrations.size();

            // Filtrer les mauvaises entrées
            List<Registration> cleaned = new ArrayList<>();
            for (Registration r : registrations) {
                // Garder seulement les inscriptions avec IDs valides
                if (r.getChildId() > 0 && r.getTimeSlotId() > 0 && r.getParentId() > 0) {
                    cleaned.add(r);
                } else {
                    System.out.println("[DataCleanupUtil] ❌ Suppression inscription #" + r.getId()
                        + " (childId=" + r.getChildId() + ", timeSlotId=" + r.getTimeSlotId()
                        + ", parentId=" + r.getParentId() + ")");
                }
            }

            // Réécrire le fichier nettoyé
            mapper.writerWithDefaultPrettyPrinter()
                  .writeValue(file, cleaned);

            int countAfter = cleaned.size();
            System.out.println("[DataCleanupUtil] ✅ registration.json nettoyé: "
                + countBefore + " → " + countAfter + " entrées");

        } catch (Exception e) {
            System.err.println("[DataCleanupUtil] Erreur lors du nettoyage registration.json");
            e.printStackTrace();
        }
    }

    /**
     * ✅ Nettoie children.json - ajoute parentId pour les enfants orphelins
     */
    public static void cleanChildrenData() {
        try {
            String childrenPath = AppConfig.getInstance().getChildrenFilePath();
            File file = new File(childrenPath);

            if (!file.exists()) {
                System.out.println("[DataCleanupUtil] children.json n'existe pas.");
                return;
            }

            List<Child> children = mapper.readValue(
                file,
                new TypeReference<List<Child>>() {}
            );

            int fixed = 0;
            for (Child c : children) {
                if (c.getParentId() <= 0) {
                    // Chercher le parent dans parents.json
                    int parentId = findParentForChild(c.getId());
                    if (parentId > 0) {
                        c.setParentId(parentId);
                        fixed++;
                        System.out.println("[DataCleanupUtil] ✅ Enfant "
                            + c.getFirstName() + " → parentId=" + parentId);
                    }
                }
            }

            if (fixed > 0) {
                mapper.writerWithDefaultPrettyPrinter()
                      .writeValue(file, children);
                System.out.println("[DataCleanupUtil] " + fixed + " enfant(s) réparé(s)");
            }

        } catch (Exception e) {
            System.err.println("[DataCleanupUtil] Erreur lors du nettoyage children.json");
            e.printStackTrace();
        }
    }

    /**
     * ✅ Génère un rapport d'incohérences
     */
    public static void reportInconsistencies() {
        try {
            RegistrationRepository regRepo = new RegistrationRepository();
            ChildRepository childRepo = new ChildRepository();
            TimeSlotRepository slotRepo = new TimeSlotRepository();

            List<Registration> registrations = regRepo.findAll();
            List<Child> children = childRepo.findAll();

            System.out.println("\n═══ RAPPORT D'INCOHÉRENCES ═══");
            System.out.println("Inscriptions totales: " + registrations.size());
            System.out.println("Enfants totaux: " + children.size());

            int invalid = 0;
            int missing = 0;

            for (Registration r : registrations) {
                if (r.getChildId() <= 0 || r.getTimeSlotId() <= 0) {
                    System.out.println("❌ Inscription #" + r.getId() + " invalide");
                    invalid++;
                }

                Child c = childRepo.findAll().stream()
                    .filter(ch -> ch.getId() == r.getChildId())
                    .findFirst()
                    .orElse(null);

                if (c == null) {
                    System.out.println("❌ Enfant manquant pour inscription #" + r.getId());
                    missing++;
                }
            }

            System.out.println("\nRésultat: " + invalid + " inscription(s) invalide(s)");
            System.out.println("Résultat: " + missing + " enfant(s) manquant(s)");
            System.out.println("═════════════════════════════\n");

        } catch (Exception e) {
            System.err.println("[DataCleanupUtil] Erreur lors du rapport");
            e.printStackTrace();
        }
    }

    /**
     * Cherche le parent d'un enfant en scannant parents.json
     */
    private static int findParentForChild(int childId) {
        try {
            String parentsPath = AppConfig.getInstance().getParentsFilePath();
            File file = new File(parentsPath);

            if (!file.exists()) return 0;

            // Charger les parents
            Models.Parent[] parents = mapper.readValue(file, Models.Parent[].class);

            for (Models.Parent p : parents) {
                for (Child c : p.getChildren()) {
                    if (c.getId() == childId) {
                        return p.getId();
                    }
                }
            }
        } catch (Exception e) {
            // Silencieux
        }
        return 0;
    }
}
