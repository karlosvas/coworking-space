package com.grupo05.coworking_space.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ReservationStatus {
    PENDING("Pending"),
    CONFIRMED("Confirmed"),
    CANCELED("Canceled"),
    COMPLETED("Completed");

    private String state;

    ReservationStatus(String state) {
        this.state = state;
    }

    @JsonCreator
    public static ReservationStatus fromString(String value) {
        for (ReservationStatus status : ReservationStatus.values()) {
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
