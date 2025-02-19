package com.grupo05.coworking_space.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.grupo05.coworking_space.model.Reservation;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    boolean existsByDescription(String description);

    boolean existsByIdAndDescription(Integer id, String description);

    boolean existsById(int id);
}