package com.grupo05.coworking_space.repository;

import com.grupo05.coworking_space.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<Usuario,Integer> {

    Optional<Usuario> findByUsername(String username);
}
