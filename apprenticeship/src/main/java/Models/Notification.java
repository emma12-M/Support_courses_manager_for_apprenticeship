package models;

import java.time.LocalDateTime;

public class Notification {

    private int id;

    private String message;

    private LocalDateTime sentDate;

    public Notification() {
    }

    public Notification(int id,
                        String message,
                        LocalDateTime sentDate) {

        this.id = id;
        this.message = message;
        this.sentDate = sentDate;
    }

    // GETTERS & SETTERS
}
