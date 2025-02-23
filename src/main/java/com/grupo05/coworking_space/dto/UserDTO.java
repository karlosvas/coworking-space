package com.grupo05.coworking_space.dto;

import com.grupo05.coworking_space.enums.Role;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Data Transfer Object para Usuarios")
public class UserDTO {
    @Schema(description = "Identificador único para usuario", type = "integer", required = false, hidden = true)
    private int id;

    @Schema(description = "Nombre del usuario", example = "Juan Pérez", type = "string", required = true)
    private String username;

    @Schema(description = "Correo electrónico del usuario", example = "juan.perez@example.com", type = "string", required = true)
    private String email;

    @Schema(description = "Contraseña del usuario", example = "password123", type = "string", required = true)
    private String password;

    @Schema(description = "Token de autenticación, no es obligatorio porque nos e requeire para registarrse", type = "string", required = false, hidden = true)
    private String token;

    @Schema(description = "Rol del usuario", type = "string", required = false, allowableValues = { "ROLE_USER",
            "ROLE_ADMIN" }, defaultValue = "ROLE_USER", hidden = true)
    private Role role;

    public UserDTO(int id, String username, String email, String password, Role role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }
}