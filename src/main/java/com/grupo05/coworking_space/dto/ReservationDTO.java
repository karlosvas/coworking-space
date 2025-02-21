package com.grupo05.coworking_space.dto;

import java.io.Serializable;
import java.util.Date;

import com.grupo05.coworking_space.enums.ReservationStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ReservationDTO implements Serializable {
    private final int id;
    private Date dateInit;
    private Date dateEnd;
    private ReservationStatus reserveStatus;
    private String description;
}