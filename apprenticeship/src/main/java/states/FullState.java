package main.java.states;

import main.java.exceptions.CapacityException;
import main.java.interfaces.ITimeSlotState;
import main.java.Models.TimeSlot;

public class FullState
        implements ITimeSlotState {

    @Override
    public void register(TimeSlot timeSlot) throws CapacityException {
        throw new CapacityException(
                "No more places available"
        );
    }
}
