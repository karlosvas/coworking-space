package com.grupo05.coworking_space.repository;

import com.grupo05.coworking_space.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Repository es una anotación de Spring que indica que la clase anotada es un "repositorio",
 * que incluye la capacidad de almacenar, recuperar y buscar entidades. Se utiliza para encapsular el almacenamiento,
 * la recuperación y la búsqueda de entidades.
 * ReservaRepository es una interfaz que extiende JpaRepository, que es una interfaz de Spring Data JPA, para trabajar con la entidad.
 */
@Repository 
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
	/**
	 *  Método que permite verificar si existe una reserva con la descripción proporcionada.
	 * @param description Descripción de la reserva
	 * @return Verdadero si existe una reserva con la descripción proporcionada, falso en caso contrario
	 */
	boolean existsByDescription(String description);

	/**
	 * Método que permite verificar si existe una reserva con el identificador y la descripción proporcionados.
	 * @param id Identificador de la reserva
	 * @param description Descripción de la reserva
	 * @return Verdadero si existe una reserva con el identificador y la descripción proporcionados, falso en caso contrario
	 */
	boolean existsByIdAndDescription(Integer id, String description);

	/**
	 * Método que permite verificar si existe una reserva con el identificador proporcionado.
	 * @param id Identificador de la reserva
	 * @return Verdadero si existe una reserva con el identificador proporcionado, falso en caso contrario
	 */
	boolean existsById(int id);

	/**
	 * Busca reservas que se solapan con un rango de fechas específico.
	 * 
	 * La consulta encuentra reservas donde:
	 * - La fecha de inicio de la reserva es menor o igual a la fecha final del rango.
	 * - La fecha de fin de la reserva es mayor o igual a la fecha inicial del rango.
	 *
	 * @param dateInit Fecha de inicio del rango de búsqueda (inclusive)
	 * @param dateEnd  Fecha de fin del rango de búsqueda (inclusive)
	 * @return Lista de reservas que se solapan con el rango especificado.
	 */
	@Query(
		value = "SELECT * FROM coworking_space.reservation r WHERE r.start_date <= :dateEnd AND r.end_date >= :dateInit",
		nativeQuery = true
	)
	List<Reservation> findReservationsBetweenDates(
		@Param("dateInit") LocalDateTime dateInit,
		@Param("dateEnd") LocalDateTime dateEnd
	);
}