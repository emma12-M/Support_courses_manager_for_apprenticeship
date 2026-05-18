package interfaces;

import Models.TimeSlot;
import exceptions.CapacityException;

public interface ITimeSlotState {

    void register(TimeSlot timeSlot) throws CapacityException;
}

