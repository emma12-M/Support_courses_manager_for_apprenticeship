package models;

import java.util.ArrayList;
import java.util.List;

public class Parent extends User {

    private List<Child> children;

    public Parent() {
        children = new ArrayList<>();
    }

    public Parent(int id,
                  String firstName,
                  String lastName,
                  String email,
                  String password) {

        super(id, firstName, lastName, email, password);

        children = new ArrayList<>();
    }

    public void addChild(Child child) {
        children.add(child);
    }

    @Override
    public void displayMenu() {
        System.out.println("Parent Dashboard");
    }

    public List<Child> getChildren() {
        return children;
    }

    public void setChildren(List<Child> children) {
        this.children = children;
    }
}
