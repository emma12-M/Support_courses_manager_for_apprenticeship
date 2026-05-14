package main.java.repositories;

import main.java.Models.Parent;

import java.util.ArrayList;
import java.util.List;

public class ParentRepository {

    private List<Parent> parents;

    public ParentRepository() {

        parents = new ArrayList<>();
    }

    public void save(Parent parent) {

        parents.add(parent);
    }

    public List<Parent> findAll() {

        return parents;
    }

    public Parent findByEmail(String email) {

        for(Parent parent : parents) {

            if(parent.getEmail().equals(email)) {

                return parent;
            }
        }

        return null;
    }
}