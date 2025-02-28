package com.grupo05.coworking_space.model;

import com.grupo05.coworking_space.enums.Role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * Clase que representa un usuario del sistema.
 * Esta clase define los atributos y relaciones de un usuario,
 * incluyendo el nombre de usuario, el correo electrónico, la contraseña
 * y el rol de usuario.
 * @Entity es una anotación de JPA que indica que la clase es una entidad.
 * @Table es una anotación de JPA que indica la tabla de base de datos a la que se asigna la entidad.
 * @Data es una anotación de Lombok que genera automáticamente los métodos equals, hashCode, toString y otros.
 */
@Entity(name = "USER")
@Table(name = "USER", schema = "coworking_space")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", unique = true)
    private Integer id;

    @Column(name = "username", nullable = false, length = 50)
    private String username;

    @Column(name = "email", nullable = false, length = 100, unique = true)
    private String email;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "role", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private Role role = Role.ROLE_USER;
}
