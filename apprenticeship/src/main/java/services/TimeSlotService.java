package main.java.services;

import main.java.Models.TimeSlot;
import main.java.repositories.TimeSlotRepository;

import java.util.List;

public class TimeSlotService {

    private TimeSlotRepository
            timeSlotRepository;

    public TimeSlotService() {

        timeSlotRepository =
                new TimeSlotRepository();
    }

    public List<TimeSlot> getAllTimeSlots() {

        return timeSlotRepository.findAll();
    }

    public void addTimeSlot(
            TimeSlot timeSlot) {

        timeSlotRepository.save(
                timeSlot
        );
    }
}