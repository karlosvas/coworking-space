package com.grupo05.coworking_space.controller;

import com.grupo05.coworking_space.annotations.SwaggerApiResponses;
import com.grupo05.coworking_space.dto.UserDTO;
import com.grupo05.coworking_space.enums.ApiSuccess;
import com.grupo05.coworking_space.enums.Role;
import com.grupo05.coworking_space.exception.RequestException;
import com.grupo05.coworking_space.service.UserDetailsServiceImpl;
import com.grupo05.coworking_space.utils.DataResponse;
import com.grupo05.coworking_space.utils.ResponseHandler;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@Tag(name = "User", description = "Endpoints para gestionar los usuarios")
public class UserController {

    private UserDetailsServiceImpl userService;

    public UserController(UserDetailsServiceImpl userService) {
        this.userService = userService;
    }

    @Operation(summary = "Registrar usuario", description = "Registra un usuario y genera un token")
    @SwaggerApiResponses
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DataResponse.class))),
    })
    @PostMapping("/register")
    public ResponseEntity<DataResponse> registrarUsuario(@RequestBody UserDTO user) {
        // Si el usuario exxiste ya esta registardo devolbemos un mensaje de está
        // registrado, si no es igual a null
        try {
            userService.findByUsername(user.getUsername());
            return ResponseHandler.handleApiResponse(ApiSuccess.RESOURCE_ALREADY_REGISTERED, user);
        } catch (RequestException e) {
            // Si no existe el usuario lo registramos
            // Si no esta registrado lo registramos
            userService.registrerUser(user, Role.ROLE_USER);
            return ResponseHandler.handleApiResponse(ApiSuccess.RESOURCE_REGISTERED, user);
        }
    }

    @Operation(summary = "Logear usuario", description = "Logea un usuario y genera un token")
    @SwaggerApiResponses
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login de usuario requiere haberse registrado previamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DataResponse.class))),
    })
    @PostMapping("/login")
    public ResponseEntity<DataResponse> loginUsuario(@RequestBody UserDTO userRequest) {
        userService.findByUsername(userRequest.getUsername());
        // Logeamos al usuario y devolvemos un mensaje de usuario logeado junto con el
        UserDTO newUserDTO = userService.loginUser(userRequest);
        return ResponseHandler.handleApiResponse(ApiSuccess.USER_LOGGED_IN, newUserDTO);
    }

    @Operation(summary = "Obtener usuario por id", description = "Obtiene un usuario por su id")
    @SwaggerApiResponses
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recursos de el usuario, requiere haver obtenido un token al logearse previamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DataResponse.class))),
    })
    @GetMapping("/resources")
    public String home() {
        return "Home";
    }

    @Operation(summary = "Obtener todos los usuarios", description = "Devuelve una lista con todos los usuarios")
    @SwaggerApiResponses
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recursos de todos los usuarios, solo para administradores", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DataResponse.class))),
    })
    @GetMapping("/list")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DataResponse> getAllUsers() {
        List<UserDTO> allUsers = userService.findAllUser();

        if (allUsers.isEmpty())
            return ResponseHandler.handleApiResponse(ApiSuccess.RESOURCE_NO_CONTENT, allUsers);

        return ResponseHandler.handleApiResponse(ApiSuccess.RESOURCE_RETRIEVED, allUsers);
    }

    @Operation(summary = "Eliminar usuario", description = "Elimina un usuario por su ID")
    @SwaggerApiResponses
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuario eliminad@", content = @Content)
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DataResponse> deleteRoom(@PathVariable("id") int id) {
        userService.deleteUserByID(id);
        return ResponseHandler.handleApiResponse(ApiSuccess.RESOURCE_REMOVED, null);
    }

    @Operation(summary = "Registrar administrador", description = "Registra un administrador y genera un token")
    @SwaggerApiResponses
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Registro de administrador, solo para administradores", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DataResponse.class))),
    })
    @PostMapping("/admin/register")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DataResponse> registrarAdmin(@RequestBody UserDTO user) {
        UserDTO newAdmin = userService.registrerUser(user, Role.ROLE_ADMIN);
        return ResponseHandler.handleApiResponse(ApiSuccess.RESOURCE_REGISTERED, newAdmin);
    }
}
