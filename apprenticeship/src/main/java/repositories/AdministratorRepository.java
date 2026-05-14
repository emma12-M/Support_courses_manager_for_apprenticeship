package main.java.repositories;

import main.java.Models.Administrator;

import java.util.ArrayList;
import java.util.List;

public class AdministratorRepository {

    private List<Administrator> administrators;

    public AdministratorRepository() {

        administrators = new ArrayList<>();
    }

    public void save(Administrator administrator) {

        administrators.add(administrator);
    }

    public List<Administrator> findAll() {

        return administrators;
    }
}