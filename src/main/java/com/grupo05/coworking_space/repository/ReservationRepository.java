package com.grupo05.coworking_space.repository;

import com.grupo05.coworking_space.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
	boolean existsByDescription(String description);

	boolean existsByIdAndDescription(Integer id, String description);

	boolean existsById(int id);

	@Query("SELECT r FROM RESERVATION r WHERE r.dateInit <= :dateEnd AND r.dateEnd >= :dateInit")
	List<Reservation> findReservationsBetweenDates(
		@Param("dateInit") LocalDateTime dateInit,
		@Param("dateEnd") LocalDateTime dateEnd
	);
}