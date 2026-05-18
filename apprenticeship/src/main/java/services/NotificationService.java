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

    /**
     * Crée et enregistre un observateur pour un parent donné.
     * Raccourci utilisé par ManagementFacade.
     *
     * @param parentId   L'ID du parent (pour la persistance)
     * @param parentName Le nom complet du parent (pour les logs)
     */
    public void subscribeParent(int parentId, String parentName) {
        ParentObserver observer = new ParentObserver(parentId, parentName);
        addObserver(observer);
    }

    /**
     * Réinitialise la liste des observateurs.
     * Utile entre deux inscriptions pour ne pas notifier deux fois.
     */
    public void clearObservers() {
        observers.clear();
    }
}

