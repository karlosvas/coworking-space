package com.grupo05.coworking_space.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.grupo05.coworking_space.model.Room;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {
}
