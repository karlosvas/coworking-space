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
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PreRemove;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


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

    @NotNull(message = "{field.null}")
    @FutureOrPresent(message = "{reservation.date}")
    @Column(name = "start_date", nullable = false)
    private LocalDateTime dateInit;

    @NotNull(message = "{field.null}")
    @FutureOrPresent(message = "{reservation.date}")
    @Column(name = "end_date", nullable = false)
    private LocalDateTime dateEnd;

    @NotNull(message = "{field.null}")
    @Enumerated(EnumType.STRING)
    @Column(name = "reservation_status", nullable = false)
    private ReservationStatus reserveStatus;

    @Size(max = 255, message = "{reservation.max.description}")
    @Column(name = "description", nullable = true)
    private String description;

    /**
     * @MayToOne es una anotación de JPA que indica que la relación entre las entidades
     * es de uno a uno. En este caso, una reserva solo puede tener un usuario asociado.
     * @JoinColumn es una anotación de JPA que indica la columna de la tabla de la base de
     * datos que se utilizará para la relación.
     * @param user es el usuario que realizó la reserva.
     * @Valid es una anotación de Bean Validation que indica que la validación debe aplicarse a la propiedad.
     */
    @Valid
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private User user;

    /**
     * @OneToMany es una anotación de JPA que indica que la relación entre las entidades
     * es de uno a muchos. En este caso, una reserva puede tener varias salas asociadas.
     * CascadeType.PERSIST y CascadeType.MERGE son opciones de cascada que indican que
     * las operaciones de persistencia y fusión deben propagarse a las entidades asociadas.
     * 
     * @param room es la lista de salas reservadas.
     * @NotEmpty es una anotación de validación que indica que la lista de salas no puede
     */
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "reservation_room",
        joinColumns = @JoinColumn(name = "reservation_id"),
        inverseJoinColumns = @JoinColumn(name = "room_id")
    )
    private List<Room> rooms;

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
        for (int i = 0; i < rooms.size(); i++) {
            roomsFK.add(rooms.get(i).getId());
        }
        return roomsFK;
    }

    @PreRemove
    private void preRemove() {
        // Eliminar this de la lista de reservas de cada sala
        for (Room room : new ArrayList<>(rooms)) {
            room.getReservations().remove(this);
        }
        // Limpiar la lista de salas
        rooms.clear();
    }

}
