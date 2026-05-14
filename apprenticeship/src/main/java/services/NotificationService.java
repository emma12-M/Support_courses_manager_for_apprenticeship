package main.java.services;

import main.java.interfaces.Observer;
import main.java.interfaces.Subject;

import java.util.ArrayList;
import java.util.List;

public class NotificationService
        implements Subject {

    private List<Observer> observers;

    public NotificationService() {

        observers = new ArrayList<>();
    }

    @Override
    public void addObserver(
            Observer observer) {

        observers.add(observer);
    }

    @Override
    public void removeObserver(
            Observer observer) {

        observers.remove(observer);
    }

    @Override
    public void notifyObservers(
            String message) {

        for(Observer observer : observers) {

            observer.update(message);
        }
    }
}