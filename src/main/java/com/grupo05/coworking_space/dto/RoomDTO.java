package com.grupo05.coworking_space.dto;

import java.io.Serializable;

import com.grupo05.coworking_space.enums.RoomStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoomDTO implements Serializable {
	private int id;
	private String name;
	private RoomStatus roomStatus;
	private int capacity;
}
