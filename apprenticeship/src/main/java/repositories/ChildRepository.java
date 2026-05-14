package main.java.repositories;

import main.java.Models.Child;

import java.util.ArrayList;
import java.util.List;

public class ChildRepository {

    private List<Child> children;

    public ChildRepository() {

        children = new ArrayList<>();
    }

    public void save(Child child) {

        children.add(child);
    }

    public List<Child> findAll() {

        return children;
    }
}