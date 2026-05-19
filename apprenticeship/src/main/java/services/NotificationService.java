package services;

import interfaces.Observer;
import interfaces.Subject;
import observers.ParentObserver;

import java.util.ArrayList;
import java.util.List;

/**
 * PATTERN OBSERVER — Service de notifications.
 *
 * Ce service joue le rôle de "Subject" (la source des événements).
 * Les parents s'y abonnent via addObserver().
 * Quand notifyObservers(message) est appelé, tous les parents abonnés
 * reçoivent la notification.
 *
 * CORRECTION : on peut maintenant créer un observateur directement
 * depuis un parent (avec son ID) via createParentObserver().
 */
public class NotificationService implements Subject {

    private List<Observer> observers;

    public NotificationService() {
        observers = new ArrayList<>();
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(String message) {
        for (Observer observer : observers) {
            observer.update(message);
        }
    }

    public void subscribeParent(int parentId, String parentName) {
        ParentObserver observer = new ParentObserver(parentId, parentName);
        addObserver(observer);
    }

    public void clearObservers() {
        observers.clear();
    }

    /**
     * ✅ NOUVEAU - Envoie une notification ciblée à UN parent
     */
    public void notifyParent(int parentId, String message) {
        ParentObserver observer = new ParentObserver(parentId, "Parent #" + parentId);
        observer.update(message);
    }

    /**
     * ✅ NOUVEAU - Envoie une notification à plusieurs parents
     */
    public void notifyParents(List<Integer> parentIds, String message) {
        for (int parentId : parentIds) {
            notifyParent(parentId, message);
        }
    }

    /**
     * ✅ NOUVEAU - Envoie une notification à TOUS les parents abonnés
     * (utilise le vieux pattern Observer, mais maintenant avec intention claire)
     */
    public void notifyAllSubscribedParents(String message) {
        notifyObservers(message);
    }
}