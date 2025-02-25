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
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.grupo05.coworking_space.enums.ReservationStatus;

@Entity(name = "RESERVATION")
@Table(name = "RESERVATION")
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

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL)
    private List<Room> room;

    public int getUserFK() {
        return user.getId();
    }

    public List<Integer> getRoomsFK() {
        List<Integer> roomsFK = new ArrayList<>();
        for (int i = 0; i < room.size(); i++) {
            roomsFK.add(room.get(i).getId());
        }
        return roomsFK;
    }
}
