package com.grupo05.coworking_space.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.grupo05.coworking_space.enums.ReservationStatus;

/**
 * Clase que representa una reserva de espacios de coworking.
 * Esta clase define los atributos y relaciones de una reserva,
 * incluyendo la fecha de inicio y fin, el estado de la reserva,
 * la descripción y los espacios reservados.
 * 
 * @Entity es una anotación de JPA que indica que la clase es una entidad.
 * @Table es una anotación de JPA que indica la tabla de base de datos a la que se asigna la entidad.
 * @Data es una anotación de Lombok que genera automáticamente los métodos equals, hashCode, toString y otros.
 */
@Entity(name = "RESERVATION")
@Table(name = "RESERVATION", schema = "coworking_space")
@Data
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id", unique = true)
    private int id;

    @NotNull(message = "Deve ingresar una fecha de inicio")
    @Column(name = "start_date", nullable = false)
    private LocalDateTime dateInit;

    @NotNull(message = "Deve ingresar una fecha de fin")
    @Column(name = "end_date", nullable = false)
    private LocalDateTime dateEnd;

    @NotNull(message = "El estado no puede estar vacío")
    @Enumerated(EnumType.STRING)
    @Column(name = "reservation_status", nullable = false)
    private ReservationStatus reserveStatus;

    @Size(max = 255, message = "La descripción no puede exceder los 255 caracteres")
    @Column(name = "description", nullable = true)
    private String description;

    /**
     * @MayToOne es una anotación de JPA que indica que la relación entre las entidades
     * es de uno a uno. En este caso, una reserva solo puede tener un usuario asociado.
     * @JoinColumn es una anotación de JPA que indica la columna de la tabla de la base de
     * datos que se utilizará para la relación.
     * @param user es el usuario que realizó la reserva.
     */
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private User user;

    /**
     * @OneToMany es una anotación de JPA que indica que la relación entre las entidades
     * es de uno a muchos. En este caso, una reserva puede tener varias salas asociadas.
     * @param room es la lista de salas reservadas.
     */
    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL)
    private List<Room> room;

    /**
     * Método que permite obtener el identificador del usuario asociado a la reserva.
     * @return el identificador del usuario asociado a la reserva.
     */
    public int getUserFK() {
        return user.getId();
    }

    /**
     * Método que permite obtener los identificadores de las salas asociadas a la reserva.
     * @return la lista de identificadores de las salas asociadas a la reserva.
     */
    public List<Integer> getRoomsFK() {
        List<Integer> roomsFK = new ArrayList<>();
        for (int i = 0; i < room.size(); i++) {
            roomsFK.add(room.get(i).getId());
        }
        return roomsFK;
    }
}
