package com.grupo05.coworking_space.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.grupo05.coworking_space.model.Room;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {
    Optional<Room> findByName(String name);

    boolean existsByNameAndIdNot(String name, int id);
}
