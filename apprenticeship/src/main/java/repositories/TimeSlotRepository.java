package main.java.repositories;

import main.java.Models.TimeSlot;

import java.util.ArrayList;
import java.util.List;

public class TimeSlotRepository {

    private List<TimeSlot> timeSlots;

    public TimeSlotRepository() {

        timeSlots = new ArrayList<>();
    }

    public void save(TimeSlot timeSlot) {

        timeSlots.add(timeSlot);
    }

    public List<TimeSlot> findAll() {

        return timeSlots;
    }
}