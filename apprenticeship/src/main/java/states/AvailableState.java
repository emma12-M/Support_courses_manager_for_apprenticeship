package main.java.states;

import main.java.interfaces.ITimeSlotState;
import main.java.Models.TimeSlot;

public class AvailableState
        implements ITimeSlotState {

    @Override
    public void register(TimeSlot timeSlot) {

        System.out.println(
                "Registration accepted"
        );

        int currentSize =
                timeSlot.getRegistrations().size();

        if(currentSize >= timeSlot.getMaxCapacity()) {

            timeSlot.setState(new FullState());

            System.out.println(
                    "Time slot is now full"
            );
        }
    }
}