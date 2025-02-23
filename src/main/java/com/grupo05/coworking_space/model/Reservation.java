package com.grupo05.coworking_space.model;

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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import com.grupo05.coworking_space.enums.ReservationStatus;

@Entity(name = "RESERVATION")
@Table(name = "RESERVATION", schema = "coworking_space")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id", nullable = false)
    private int id;

    @NotNull(message = "Deve ingresar una fecha de inicio")
    @Column(name = "init_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateInit;

    @NotNull(message = "Deve ingresar una fecha de fin")
    @Column(name = "end_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateEnd;

    @NotNull(message = "El estado no puede estar vacío")
    @Enumerated(EnumType.STRING)
    @Column(name = "reserve_status", nullable = true)
    private ReservationStatus reserveStatus;

    @Size(max = 255, message = "La descripción no puede exceder los 255 caracteres")
    @Column(name = "description", nullable = true)
    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "reservation")
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
