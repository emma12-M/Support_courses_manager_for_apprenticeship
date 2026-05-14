package main.java.Models;

import main.java.enums.TimeSlotState;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TimeSlot {

    private int id;

    private String subject;

    private LocalDateTime schedule;

    private ClassRoom classroom;

    private int maxCapacity;

    private TimeSlotState state;

    private List<Registration> registrations;

    public TimeSlot() {

        registrations = new ArrayList<>();
    }

    public TimeSlot(int id,
                    String subject,
                    LocalDateTime schedule,
                    ClassRoom classroom,
                    int maxCapacity) {

        this.id = id;
        this.subject = subject;
        this.schedule = schedule;
        this.classroom = classroom;
        this.maxCapacity = maxCapacity;

        this.state = TimeSlotState.AVAILABLE;

        registrations = new ArrayList<>();
    }

    public boolean isFull() {
        return registrations.size() >= maxCapacity;
    }

    // GETTERS & SETTERS
}
