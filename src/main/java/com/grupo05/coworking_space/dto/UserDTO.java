package com.grupo05.coworking_space.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Data Transfer Object para Usuarios")
public class UserDTO {
    @Schema(description = "Identificador único para usuario", type = "integer", required = true, hidden = true)
    private int id;

    @Schema(description = "Nombre del usuario", example = "Juan Pérez", type = "string", required = true)
    private String username;

    @Schema(description = "Correo electrónico del usuario", example = "juan.perez@example.com", type = "string", required = true)
    private String email;

    @Schema(description = "Contraseña del usuario", example = "password123", type = "string", required = true)
    private String password;
}