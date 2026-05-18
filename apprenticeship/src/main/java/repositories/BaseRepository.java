package repositories;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe de base pour tous les repositories.
 *
 * CORRECTIONS :
 * 1. IGNORE_UNKNOWN_PROPERTIES = true → Jackson ne plante plus si le JSON
 *    contient un champ inconnu (ex: "state" dans timeSlots.json)
 * 2. WRITE_DATES_AS_TIMESTAMPS = false → les dates LocalDate/LocalDateTime
 *    sont sérialisées en format ISO String (2026-05-18) et non en tableau
 * 3. update() cherche l'élément par ID avant de remplacer (voir sous-classes)
 */
public abstract class BaseRepository<T> {

    protected List<T> items;
    protected ObjectMapper mapper;
    protected String filePath;
    protected TypeReference<List<T>> typeReference;

    public BaseRepository(String filePath, TypeReference<List<T>> typeReference) {
        this.filePath = filePath;
        this.typeReference = typeReference;

        mapper = new ObjectMapper();

        // CORRECTION 1 : support des dates Java 8 (LocalDate, LocalDateTime)
        mapper.registerModule(new JavaTimeModule());

        // CORRECTION 2 : écriture des dates en format texte (ISO-8601)
        //   Ex: "2026-05-18" au lieu de [2026,5,18]
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // CORRECTION 3 : champs inconnus dans JSON → ignorés sans erreur
        //   Ex: "state" dans timeSlots.json ne plante plus
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        load();
    }

    /**
     * Ajoute un nouvel élément et sauvegarde en JSON.
     */
    public void save(T item) {
        items.add(item);
        write();
    }

    /**
     * Met à jour un élément existant et sauvegarde en JSON.
     * Cette version de base écrit simplement la liste (elle sera surchargée
     * par les repositories qui ont besoin de remplacer un élément par ID).
     */
    public void update(T item) {
        write();
    }

    /**
     * Supprime un élément et sauvegarde en JSON.
     */
    public void delete(T item) {
        items.remove(item);
        write();
    }

    /**
     * Récupère tous les éléments.
     */
    public List<T> findAll() {
        return items;
    }

    /**
     * Recharge les données depuis le fichier JSON.
     * Utile pour rafraîchir après une modification externe.
     */
    public void refresh() {
        load();
    }

    /**
     * Écrit toute la liste en JSON dans le fichier.
     */
    protected void write() {
        try {
            // Crée le dossier parent si nécessaire
            File file = new File(filePath);
            if (file.getParentFile() != null) {
                file.getParentFile().mkdirs();
            }

            mapper.writerWithDefaultPrettyPrinter()
                  .writeValue(file, items);

        } catch (IOException e) {
            System.err.println("[BaseRepository] Erreur d'écriture dans " + filePath);
            e.printStackTrace();
        }
    }

    /**
     * Charge la liste depuis le fichier JSON.
     * Si le fichier est absent ou vide → liste vide, pas d'erreur.
     */
    protected void load() {
        File file = new File(filePath);

        if (file.exists() && file.length() > 0) {
            try {
                items = mapper.readValue(file, typeReference);
            } catch (IOException e) {
                System.err.println("[BaseRepository] Erreur de lecture dans " + filePath);
                e.printStackTrace();
                items = new ArrayList<>();
            }
        } else {
            items = new ArrayList<>();
        }
    }
}
