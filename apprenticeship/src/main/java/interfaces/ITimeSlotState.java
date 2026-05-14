package main.java.interfaces;

import main.java.Models.TimeSlot;
import main.java.exceptions.CapacityException;

public interface ITimeSlotState {

    void register(TimeSlot timeSlot) throws CapacityException;
}