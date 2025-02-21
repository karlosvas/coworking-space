package com.grupo05.coworking_space.exception;

import lombok.extern.slf4j.Slf4j;

import lombok.Getter;
import lombok.ToString;

@Slf4j
@ToString
@Getter

// TODO: Crear ReservationNotFoundException en el global handler de excepciones
public class ReservationNotFoundException extends Exception {
    private final int entityId;

    public ReservationNotFoundException(String message, Throwable cause) {
        super(message);
        this.entityId = Integer.parseInt(cause.getMessage());
        log.error("ID error ReservationNotFoundException: {}", this.getEntityId());
    }
}