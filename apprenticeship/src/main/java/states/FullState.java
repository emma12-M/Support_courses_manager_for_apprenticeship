package states;

import exceptions.CapacityException;
import interfaces.ITimeSlotState;
import Models.TimeSlot;

public class FullState
        implements ITimeSlotState {

    @Override
    public void register(TimeSlot timeSlot) throws CapacityException {
        throw new CapacityException(
                "No more places available"
        );
    }
}


