package com.grupo05.coworking_space.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Enumeración que define los posibles estados de una reserva.
 * Esta enumeración permite gestionar el ciclo de vida de las reservas,
 * desde su creación inicial hasta su finalización o cancelación.
 */
public enum ReservationStatus {
     /** Estado que indica que la reserva está pendiente de confirmación */
     PENDING("Pending"),
     /** Estado que indica que la reserva ha sido confirmada */
     CONFIRMED("Confirmed"),
     /** Estado que indica que la reserva ha sido cancelada */
     CANCELED("Canceled"),
     /** Estado que indica que la reserva se ha completado correctamente */
     COMPLETED("Completed");
    
    private String state;

    /**
     * Constructor para los valores de la enumeración.
     * @param state Texto descriptivo del estado de la reserva
     */
    ReservationStatus(String state) {
        this.state = state;
    }

    /**
     * Convierte una cadena de texto en un valor de la enumeración ReservationStatus.
     * Este método se utiliza durante la deserialización JSON para convertir
     * un valor de texto en el tipo enumerado correspondiente.
     * 
     * @param value Texto que representa el estado de reserva
     * @return El valor de enumeración correspondiente al texto proporcionado
     * @throws IllegalArgumentException Si el texto no corresponde a ningún estado válido
     * @JsonCreator es una anotación de Jackson que indica que un método es un constructor de fábr
     */
    @JsonCreator
    public static ReservationStatus fromString(String value) {
        for (ReservationStatus status : ReservationStatus.values()) {
            if (status.getState().equalsIgnoreCase(value))
                return status;
        }
        throw new IllegalArgumentException("Invalid status: " + value);
    }

    /**
     * Obtiene el texto descriptivo del estado de la reserva.
     * Este método se utiliza durante la serialización JSON para convertir
     * el valor enumerado en una representación de texto.
     * 
     * @return El texto que representa este estado de reserva
     * @JsonValue para indicar que el valor de retorno de este método debe ser serializado como un valor JSON
     */
    @JsonValue
    public String getState() {
        return state;
    }
}
