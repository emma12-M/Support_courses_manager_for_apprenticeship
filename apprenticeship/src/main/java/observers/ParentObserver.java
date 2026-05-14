package main.java.observers;

import main.java.interfaces.Observer;

public class ParentObserver
        implements Observer {

    private String parentName;

    public ParentObserver(String parentName) {

        this.parentName = parentName;
    }

    @Override
    public void update(String message) {

        System.out.println(
                "Notification sent to "
                + parentName
                + " : "
                + message
        );
    }
}