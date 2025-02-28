package com.grupo05.coworking_space.controller;

import com.grupo05.coworking_space.annotations.SwaggerApiResponses;
import com.grupo05.coworking_space.dto.ReservationDTO;
import com.grupo05.coworking_space.dto.UserDTO;
import com.grupo05.coworking_space.enums.ApiSuccess;
import com.grupo05.coworking_space.enums.Role;
import com.grupo05.coworking_space.exception.RequestException;
import com.grupo05.coworking_space.service.ReservationService;
import com.grupo05.coworking_space.service.UserDetailsServiceImpl;
import com.grupo05.coworking_space.utils.DataResponse;
import com.grupo05.coworking_space.utils.ResponseHandler;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.ArrayList;
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

/**
 * Esta clase define los endpoints para gestionar las salas, mas informacion en swagger
 * en la url localhost:8080/api/swagger-ui.html
 * @RestController para indicar que es un controlador de Spring
 * @RequestMapping para indicar la ruta de acceso a los endpoints
 * @Tag para documentar el controlador
 */
@RestController
@RequestMapping("/users")
@Tag(name = "User", description = "Endpoints para gestionar los usuarios")
public class UserController {

    /**
	 * Servicio de salas
	 * @Operation para documentar el endpoint
	 * @SwaggerApiResponses para documentar las respuestas de error la API
	 * @ApiResponses para documentar una respuestas de la API
	 * @ApiResponse para documentar una respuesta de la API
	 * @Content para documentar el tipo de contenido de la respuesta
	 * @Schema para documentar el esquema de la respuesta
	 */

    private UserDetailsServiceImpl userService;
    private ReservationService reservationService;

    /**
     * Constructor que inyecta las dependencias necesarias.
     * 
     * @param userService Servicio de usuarios
     * @param reservationService Servicio de reservas
     */
    public UserController(UserDetailsServiceImpl userService, ReservationService reservationService) {
        this.userService = userService;
        this.reservationService = reservationService;
    }

    /**
     * Registra un nuevo usuario en el sistema.
     * Verifica primero si el nombre de usuario ya existe en el sistema.
     * Si ya existe, devuelve un mensaje indicando que ya está registrado.
     * Si no existe, crea un nuevo usuario con rol USER.
     * 
     * @param user DTO con la información del usuario a registrar
     * @return ResponseEntity con el resultado de la operación
     * @RequestBody Vincula el cuerpo de la solicitud HTTP al parámetro del método
     * @PostMapping Mapea solicitudes HTTP POST a este método
     */
    @Operation(summary = "Registrar usuario", description = "Registra un usuario y genera un token")
    @SwaggerApiResponses
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DataResponse.class))),
    })
    @PostMapping("/register")
    public ResponseEntity<DataResponse> registrarUsuario(@RequestBody UserDTO user) {
        try {
            userService.findByUsername(user.getUsername());
            return ResponseHandler.handleApiResponse(ApiSuccess.RESOURCE_ALREADY_REGISTERED, user);
        } catch (RequestException e) {
            userService.registrerUser(user, Role.ROLE_USER);
            return ResponseHandler.handleApiResponse(ApiSuccess.RESOURCE_REGISTERED, user);
        }
    }

    /**
     * Autentica un usuario en el sistema.
     * Verifica las credenciales del usuario y genera un token JWT para el acceso.
     * 
     * @param userRequest DTO con las credenciales del usuario
     * @return ResponseEntity con los datos del usuario y el token JWT
     * @RequestBody Vincula el cuerpo de la solicitud HTTP al parámetro del método
     * @PostMapping Mapea solicitudes HTTP POST a este método
     */
    @Operation(summary = "Logear usuario", description = "Logea un usuario y genera un token")
    @SwaggerApiResponses
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login de usuario requiere haberse registrado previamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DataResponse.class))),
    })
    @PostMapping("/login")
    public ResponseEntity<DataResponse> loginUsuario(@RequestBody UserDTO userRequest) {
        // Llamamos a find by username para comprobar si el usuario existe ya que si no lo hace lanzara una excepcion
        userService.findByUsername(userRequest.getUsername());
        // Logeamos al usuario y devolvemos un mensaje de usuario logeado junto con el
        UserDTO newUserDTO = userService.loginUser(userRequest);
        return ResponseHandler.handleApiResponse(ApiSuccess.USER_LOGGED_IN, newUserDTO);
    }

    /**
     * Obtiene las reservas asociadas a un usuario específico.
     * Filtra todas las reservas del sistema para mostrar solo las asociadas al ID de usuario proporcionado.
     * 
     * @param id ID del usuario cuyas reservas se quieren consultar
     * @return ResponseEntity con la lista de reservas del usuario
     * @PathVariable Vincula el parámetro de la URL al parámetro del método
     * @GetMapping Mapea solicitudes HTTP GET a este método
     */
    @Operation(summary = "Recursos del usuario", description = "Obtene todos los recursos de el usuario, como sus reservas actuales")
    @SwaggerApiResponses
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recursos de el usuario, requiere haver obtenido un token al logearse previamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DataResponse.class))),
    })
    @GetMapping("/resources/{id}")
    public ResponseEntity<DataResponse>  findReservationsById(@PathVariable("id") int id) {
        // Obtenemos todas las reservas
        List<ReservationDTO> allResergvations = reservationService.findAllReservations();

        // Creamos una nueva lista para almacenar las reservas del usuario
        List<ReservationDTO> newAllReservation = new ArrayList<>();
        
        // Comprobamos si la reserva pertenece al usuario, si es asi la añadimos a la lista
        for (ReservationDTO reservationDTO : allResergvations) 
            if (reservationDTO.getUserFK() == id) 
                newAllReservation.add(reservationDTO);

        if(newAllReservation.isEmpty())
            return ResponseHandler.handleApiResponse(ApiSuccess.RESOURCE_NO_CONTENT, newAllReservation);

        return ResponseHandler.handleApiResponse(ApiSuccess.RESOURCE_RETRIEVED , newAllReservation);
    }

     /**
     * Obtiene una lista de todos los usuarios registrados en el sistema.
     * Este endpoint está restringido a usuarios con rol de ADMIN.
     * 
     * @return ResponseEntity con la lista de usuarios
     * @GetMapping Mapea solicitudes HTTP GET a este método
     * @PreAuthorize Restringe el acceso solo a usuarios con rol ADMIN
     */
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

     /**
     * Elimina un usuario del sistema por su ID.
     * Este endpoint está restringido a usuarios con rol de ADMIN.
     * 
     * @param id ID del usuario a eliminar
     * @return ResponseEntity con mensaje de confirmación
     * @PathVariable Vincula el parámetro de la URL al parámetro del método
     * @DeleteMapping Mapea solicitudes HTTP DELETE a este método
     * @PreAuthorize Restringe el acceso solo a usuarios con rol ADMIN
     */
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

    /**
     * Registra un nuevo administrador en el sistema.
     * <p>
     * Este endpoint está restringido a usuarios con rol de ADMIN.
     * 
     * @param user DTO con la información del usuario administrador a registrar
     * @return ResponseEntity con los datos del administrador registrado
     * @RequestBody Vincula el cuerpo de la solicitud HTTP al parámetro del método
     * @PostMapping Mapea solicitudes HTTP POST a este método
     * @PreAuthorize Restringe el acceso solo a usuarios con rol ADMIN
     */
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
