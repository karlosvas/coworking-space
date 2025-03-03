package com.grupo05.coworking_space.dto;

import com.grupo05.coworking_space.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object para Usuarios
 * @Schema Permite configurar la documentación de Swagger para este DTO
 * @Getter, @Setter y @AllArgsConstructor, @NoArgsConstructor son anotaciones de Lombok para generar automáticamente los getters, setters y constructores con todos los argumentos, y sin argumentos
 * Para más infromacion sobre el dto, ver la documentacion de Swagger en: localhost:8080/api/swagger-ui.html
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Data Transfer Object para Usuarios")
public class UserDTO {
    @Schema(description = "Identificador único para usuario", type = "integer", required = false, hidden = true)
    private int id;

    @Schema(description = "Nombre del usuario", example = "Jorge", type = "string", required = true)
    @NotNull(message = "{field.null}")
    @NotEmpty(message = "{field.empty}")
    @Size(min = 2, message = "{user.username.size}")
    private String username;

    @Schema(description = "Correo electrónico del usuario", example = "jorge@gmail.com", type = "string", required = true)
    @NotNull(message = "{field.null}")
    @NotEmpty(message = "{field.empty}")
    @Email(message = "{user.email}")
    private String email;

    @Schema(description = "Contraseña del usuario", example = "jorge123", type = "string", required = true)
    @NotNull(message = "{field.null}")
    @NotEmpty(message = "{field.empty}")
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