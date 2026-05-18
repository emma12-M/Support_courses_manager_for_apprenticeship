package Models;

public class ClassRoom {

    private int id;
    private String name;
    private int capacity;

    // Constructeur vide — OBLIGATOIRE pour Jackson
    public ClassRoom() {
    }

    // Constructeur avec paramètres — utile pour créer une salle manuellement
    public ClassRoom(int id, String name, int capacity) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
    }

    // GETTERS — Jackson les utilise pour lire les valeurs
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCapacity() {
        return capacity;
    }

    // SETTERS — Jackson les utilise pour remplir l'objet depuis JSON
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    // Utile pour afficher dans les ComboBox JavaFX
    @Override
    public String toString() {
        return name + " (capacité: " + capacity + ")";
    }
}


