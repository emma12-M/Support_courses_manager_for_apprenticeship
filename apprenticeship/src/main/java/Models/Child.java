package Models;

import java.time.LocalDate;

/**
 * Représente un enfant inscrit à des cours de soutien.
 *
 * AJOUT : le champ parentId permet de retrouver tous les enfants
 * d'un parent directement depuis children.json, sans avoir à charger
 * tous les parents.
 */
public class Child {

    private int id;

    // ID du parent propriétaire de cet enfant
    private int parentId;

    private String firstName;

    private String lastName;

    private int age;

    private String level;

    private LocalDate birthDate;

    public Child() {
    }

    public Child(int id, int parentId, String firstName, String lastName,
                 int age, String level, LocalDate birthDate) {
        this.id = id;
        this.parentId = parentId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.level = level;
        this.birthDate = birthDate;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getParentId() { return parentId; }
    public void setParentId(int parentId) { this.parentId = parentId; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }

    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }
}
