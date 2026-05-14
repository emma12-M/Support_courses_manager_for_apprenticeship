package main.java.Models;


import main.java.exceptions.CapacityException;
import main.java.interfaces.ITimeSlotState;
import main.java.states.AvailableState;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TimeSlot {

    private int id;

    private String subject;

    private LocalDateTime schedule;

    private ClassRoom classroom;

    private int maxCapacity;

    private List<Registration> registrations;

    private ITimeSlotState state;

    public TimeSlot() {

        registrations = new ArrayList<>();

        state = new AvailableState();
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

        registrations = new ArrayList<>();

        state = new AvailableState();
    }

    public void register() {

        try {
			state.register(this);
		} catch (CapacityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public void setState(ITimeSlotState state) {

        this.state = state;
    }

    public ITimeSlotState getState() {

        return state;
    }

    public int getMaxCapacity() {

        return maxCapacity;
    }

    public List<Registration> getRegistrations() {

        return registrations;
    }

    // GETTERS & SETTERS
}
