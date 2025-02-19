package com.grupo05.coworking_space.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum StateReservation {
    PENDING("Pending"),
    CONFIRMED("Confirmed"),
    CANCELED("Canceled"),
    COMPLETED("Completed");

    private String state;

    StateReservation(String state) {
        this.state = state;
    }

    @JsonCreator
    public static StateReservation fromString(String value) {
        for (StateReservation status : StateReservation.values()) {
            if (status.getState().equalsIgnoreCase(value))
                return status;
        }
        throw new IllegalArgumentException("Invalid status: " + value);
    }

    @JsonValue
    public String getState() {
        return state;
    }
}
