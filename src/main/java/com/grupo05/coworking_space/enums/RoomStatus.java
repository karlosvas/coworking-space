package com.grupo05.coworking_space.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Enumeración que define los posibles estados de una sala de coworking.
 * Esta enumeración permite gestionar los diferentes estados en que puede
 * encontrarse una sala, facilitando su control de disponibilidad.
 */
public enum RoomStatus {
	 /** Estado que indica que la sala está disponible para ser reservada */
	 AVAILABLE("Available"),
	 /** Estado que indica que la sala está ocupada y no puede ser reservada temporalmente */
	 BUSY("Busy"),
	 /** Estado que indica que la sala está en mantenimiento y no puede ser utilizada */
	 MAINTENANCE("Maintenance"),
	 /** Estado que indica que la sala no está disponible por razones no especificadas */
	 NOT_AVAILABLE("Not Available");


	private String state;

	/**
     * Constructor para los valores de la enumeración.
     * 
     * @param state Texto descriptivo del estado de la sala
     */
	RoomStatus(String state) {
		this.state = state;
	}

	 /**
     * Convierte una cadena de texto en un valor de la enumeración RoomStatus.
     * <p>
     * Este método se utiliza durante la deserialización JSON para convertir
     * un valor de texto en el tipo enumerado correspondiente.
     * 
     * @param value Texto que representa el estado de la sala
     * @return El valor de enumeración correspondiente al texto proporcionado
     * @throws IllegalArgumentException Si el texto no corresponde a ningún estado válido
	 * @JsonCreator es una anotación de Jackson que indica que un método es un constructor de fábr
     */
	@JsonCreator
	public static RoomStatus fromString(String value) {
		for (RoomStatus status : RoomStatus.values()) {
			if (status.getState().equalsIgnoreCase(value))
				return status;
		}
		throw new IllegalArgumentException("Invalid status: " + value);
	}

	/**
     * Obtiene el texto descriptivo del estado de la sala.
     * <p>
     * Este método se utiliza durante la serialización JSON para convertir
     * el valor enumerado en una representación de texto.
     * 
     * @return El texto que representa este estado de sala
	 * @JsonValue para indicar que el valor de retorno de este método debe ser serializado como un valor JSON
     */
    @JsonValue
	public String getState() {
		return state;
	}
}
