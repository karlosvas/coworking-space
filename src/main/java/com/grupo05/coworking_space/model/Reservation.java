package com.grupo05.coworking_space.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Date;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import com.grupo05.coworking_space.enums.StateReservation;

@Entity
@Table(name = "RESERVATION")
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
    private StateReservation reserveStatus;

    @Size(max = 255, message = "La descripción no puede exceder los 255 caracteres")
    @Column(name = "description", nullable = true)
    private String description;

    // @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    // @JoinColumn(name = "id_usuario", referencedColumnName = "id")
    // private Usuario usuario;

    // @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    // @JoinColumn(name = "id_sala", referencedColumnName = "id")
    // private List<Salas> salas;
}
