package com.grupo05.coworking_space.repository;

import com.grupo05.coworking_space.enums.Role;
import com.grupo05.coworking_space.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    /**
     * Método que permite buscar un usuario por su nombre de usuario.
     * @param username Nombre de usuario
     * @return Usuario opcional con el nombre de usuario proporcionado, o vacío si no existe null
     */
    Optional<User> findByUsername(String username);

    /**
     * Método que permite buscar un usuario por su correo electrónico.
     * @param email Correo electrónico
     * @return Usuario opcional con el correo electrónico proporcionado, o vacío si no existe null
     */
    Optional<User> findByEmail(String email);

    /**
     * Método que permite buscar un usuario por su rol.
     * @param role Rol del usuario
     * @return Usuario opcional con el rol proporcionado, o vacío si no existe null
     */
    Optional<User> findByRole(Role role);

    /*
     * 
     */
    Optional<User> findByUsernameAndEmail(String username, String email);
}
